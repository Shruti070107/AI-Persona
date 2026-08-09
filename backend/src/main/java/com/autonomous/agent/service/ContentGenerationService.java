package com.autonomous.agent.service;

import com.autonomous.agent.model.Decision;
import com.autonomous.agent.model.Persona;
import com.autonomous.agent.model.PersonaProfile;
import com.autonomous.agent.model.Post;
import com.autonomous.agent.model.Topic;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Writes the actual post text and its publishing rationale, in the persona's
 * consistent editorial voice. This is intentionally template/rule-based so the
 * voice stays stable across every post (same hook style, same opinion cadence,
 * same sign-offs and hashtags) rather than drifting the way free-form
 * generation can. Swap in a call to an LLM here if a more fluid voice is
 * desired — the persona profile already carries everything a prompt would need.
 */
@Service
public class ContentGenerationService {

    private final Random random = new Random();

    public Post generate(PersonaProfile profile, Decision decision, int candidatesConsidered,
                          int acceptedCount, String postId) {
        Topic topic = decision.getTopic();

        String hook = pick(profile.getOpeningHooks());
        String stanceTemplate = pick(profile.getOpinionStances());
        String stance = stanceTemplate.replace("{topic}", topic.getTitle());
        String signoff = pick(profile.getClosingSignoffs());
        String hashtags = String.join(" ", pickTwo(profile.getHashtags()));

        StringBuilder text = new StringBuilder();
        text.append(hook).append(" ").append(topic.getTitle()).append(".\n\n");
        text.append(stance).append("\n\n");
        text.append(signoff).append("\n\n");
        text.append(hashtags);

        String rationale = buildRationale(profile, decision, candidatesConsidered, acceptedCount, topic);

        List<String> sources = new ArrayList<>();
        if (topic.getUrl() != null) sources.add(topic.getUrl());
        if (topic.getSecondarySourceUrl() != null) sources.add(topic.getSecondarySourceUrl());

        return new Post(postId, Instant.now(), text.toString(), rationale, sources, topic.key());
    }

    private String buildRationale(PersonaProfile profile, Decision decision, int candidatesConsidered,
                                   int acceptedCount, Topic topic) {
        String recency = describeRecency(topic.getPublishedAt());
        String keywordList = decision.getMatchedKeywords().isEmpty()
                ? profile.getDomain()
                : String.join(", ", decision.getMatchedKeywords());

        return String.format(Locale.ROOT,
                "Selected because it directly matches %s's core interests (%s), scoring %.2f against this " +
                        "persona's editorial threshold. It is relevant now because %s. This cycle discovered " +
                        "%d candidate topics from live sources, of which %d met the editorial bar for this " +
                        "persona; this was the strongest of them. Sourced from %s.",
                profile.getName(), keywordList, decision.getScore(), recency,
                candidatesConsidered, acceptedCount, topic.getSource());
    }

    private String describeRecency(Instant publishedAt) {
        if (publishedAt == null) return "it surfaced in this cycle's discovery pass";
        long hours = java.time.Duration.between(publishedAt, Instant.now()).toHours();
        if (hours <= 6) return "it surfaced within the last few hours";
        if (hours <= 24) return "it surfaced within the last day";
        if (hours <= 72) return "it's been active in the last few days and still trending in discussion";
        return "it remains an active topic despite not being brand new";
    }

    private String pick(List<String> options) {
        if (options == null || options.isEmpty()) return "";
        return options.get(random.nextInt(options.size()));
    }

    private List<String> pickTwo(List<String> options) {
        if (options == null || options.isEmpty()) return List.of();
        if (options.size() <= 2) return options;
        List<String> copy = new ArrayList<>(options);
        java.util.Collections.shuffle(copy, random);
        return copy.subList(0, 2);
    }
}
