package com.autonomous.agent.service;

import com.autonomous.agent.model.PersonaProfile;
import com.autonomous.agent.model.Topic;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Discovers candidate topics from live, freely-accessible information sources:
 *  - Hacker News (real-time tech/AI discussion signal, via the public Firebase API)
 *  - arXiv (recent AI/CS research papers, via the public Atom API)
 *
 * No API key is required for either source, so discovery works out of the box.
 */
@Service
public class TopicDiscoveryService {

    private static final Logger log = LoggerFactory.getLogger(TopicDiscoveryService.class);

    private static final String HN_TOP_STORIES = "https://hacker-news.firebaseio.com/v0/topstories.json";
    private static final String HN_ITEM = "https://hacker-news.firebaseio.com/v0/item/%d.json";
    private static final String ARXIV_QUERY =
            "http://export.arxiv.org/api/query?search_query=cat:%s&sortBy=submittedDate&sortOrder=descending&max_results=%d";

    private final RestTemplate restTemplate;

    @Value("${agent.discovery.hackernews.top-story-limit:20}")
    private int hnLimit;

    @Value("${agent.discovery.arxiv.max-results:10}")
    private int arxivMaxResults;

    public TopicDiscoveryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /** Discover a fresh batch of candidate topics from all live sources. */
    public List<Topic> discoverAll(PersonaProfile profile) {
        List<Topic> topics = new ArrayList<>();
        topics.addAll(safe(this::discoverHackerNews, "Hacker News"));
        topics.addAll(safe(() -> discoverArxiv(profile.getArxivCategory()), "arXiv"));
        return topics;
    }

    private interface Fetcher {
        List<Topic> fetch();
    }

    private List<Topic> safe(Fetcher fetcher, String sourceName) {
        try {
            return fetcher.fetch();
        } catch (Exception e) {
            log.warn("Topic discovery from {} failed: {}", sourceName, e.getMessage());
            return List.of();
        }
    }

    // ---------------------------------------------------------------------
    // Hacker News
    // ---------------------------------------------------------------------

    private List<Topic> discoverHackerNews() {
        Long[] ids = restTemplate.getForObject(HN_TOP_STORIES, Long[].class);
        if (ids == null) return List.of();

        List<Topic> topics = new ArrayList<>();
        int limit = Math.min(hnLimit, ids.length);
        for (int i = 0; i < limit; i++) {
            try {
                HnItem item = restTemplate.getForObject(String.format(HN_ITEM, ids[i]), HnItem.class);
                if (item == null || item.title == null) continue;
                if (item.type != null && !item.type.equals("story")) continue;

                double normalizedSignal = Math.min(1.0, (item.score == null ? 0 : item.score) / 200.0);
                Instant publishedAt = item.time != null ? Instant.ofEpochSecond(item.time) : Instant.now();
                String url = (item.url != null && !item.url.isBlank())
                        ? item.url
                        : "https://news.ycombinator.com/item?id=" + item.id;
                String discussion = "https://news.ycombinator.com/item?id=" + item.id;

                topics.add(new Topic(
                        item.title,
                        "Discussed on Hacker News" + (item.score != null ? " with " + item.score + " points" : ""),
                        url,
                        discussion,
                        "Hacker News",
                        publishedAt,
                        normalizedSignal
                ));
            } catch (Exception e) {
                log.debug("Skipping HN item {}: {}", ids[i], e.getMessage());
            }
        }
        return topics;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class HnItem {
        public Long id;
        public String title;
        public String url;
        public Long score;
        public Long time;
        public String type;
    }

    // ---------------------------------------------------------------------
    // arXiv
    // ---------------------------------------------------------------------

    private List<Topic> discoverArxiv(String category) {
        String cat = (category == null || category.isBlank()) ? "cs.AI" : category;
        String query = String.format(ARXIV_QUERY,
                URLEncoder.encode(cat, StandardCharsets.UTF_8), arxivMaxResults);

        String xml = restTemplate.getForObject(query, String.class);
        if (xml == null || xml.isBlank()) return List.of();

        List<Topic> topics = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new org.xml.sax.InputSource(new StringReader(xml)));

            NodeList entries = doc.getElementsByTagName("entry");
            for (int i = 0; i < entries.getLength(); i++) {
                Element entry = (Element) entries.item(i);
                String title = textOf(entry, "title");
                String summary = textOf(entry, "summary");
                String published = textOf(entry, "published");
                String link = firstLink(entry);

                if (title == null || title.isBlank()) continue;

                Instant publishedAt;
                try {
                    publishedAt = OffsetDateTime.parse(published).toInstant();
                } catch (Exception ex) {
                    publishedAt = Instant.now();
                }

                topics.add(new Topic(
                        collapseWhitespace(title),
                        summary != null ? collapseWhitespace(summary) : "",
                        link != null ? link : "https://arxiv.org",
                        null,
                        "arXiv",
                        publishedAt,
                        0.6 // research papers carry inherent substance regardless of "popularity"
                ));
            }
        } catch (Exception e) {
            log.warn("Failed to parse arXiv feed: {}", e.getMessage());
        }
        return topics;
    }

    private static String textOf(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagName(tag);
        if (nl.getLength() == 0) return null;
        Node node = nl.item(0);
        return node.getTextContent();
    }

    private static String firstLink(Element entry) {
        NodeList links = entry.getElementsByTagName("link");
        for (int i = 0; i < links.getLength(); i++) {
            Element link = (Element) links.item(i);
            String rel = link.getAttribute("rel");
            String href = link.getAttribute("href");
            if (href != null && !href.isBlank() && (rel == null || rel.isBlank() || rel.equals("alternate"))) {
                return href;
            }
        }
        return null;
    }

    private static String collapseWhitespace(String s) {
        return Objects.requireNonNullElse(s, "").replaceAll("\\s+", " ").trim();
    }
}
