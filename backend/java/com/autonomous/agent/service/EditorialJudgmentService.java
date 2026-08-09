package com.autonomous.agent.service;

import com.autonomous.agent.model.AgentState;
import com.autonomous.agent.model.Decision;
import com.autonomous.agent.model.PersonaProfile;
import com.autonomous.agent.model.Topic;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Applies editorial judgment to candidate topics. This is the component that
 * gives the persona a "no" — most discovered topics get rejected, either
 * because they're off-domain, too thin to say something original about, or
 * too similar to something already published.
 */
@Service
public class EditorialJudgmentService {

    private static final double WEIGHT_RELEVANCE = 0.55;
    private static final double WEIGHT_QUALITY = 0.25;
    private static final double WEIGHT_RECENCY = 0.20;

    private static final double DUPLICATE_SIMILARITY_THRESHOLD = 0.5;
    private static final double MIN_SUMMARY_LENGTH_FOR_FULL_QUALITY = 40;

    /** Evaluate a single candidate topic against a persona's editorial standards. */
    public Decision evaluate(Topic topic, PersonaProfile profile, AgentState agent) {
        if (topic == null || topic.getTitle() == null || topic.getTitle().isBlank()) {
            return new Decision(topic, false, "Empty or malformed candidate", 0.0, List.of());
        }

        // 1. Duplicate / repetition check against memory (continuity requirement).
        if (isTooSimilarToPublished(topic, agent)) {
            return new Decision(topic, false,
                    "Too similar to content already published by this persona; would be repetitive.",
                    0.0, List.of());
        }

        // 2. Relevance to the persona's declared interests.
        List<String> matched = matchedKeywords(topic, profile);
        double relevance = Math.min(1.0, matched.size() / 3.0);

        if (matched.isEmpty()) {
            return new Decision(topic, false,
                    "Off-topic for this persona's domain (" + profile.getDomain() + "); no relevant signal found.",
                    0.0, matched);
        }

        // 3. Quality / substance signal.
        double quality = topic.getSignal();
        if (topic.getSummary() != null && topic.getSummary().length() < MIN_SUMMARY_LENGTH_FOR_FULL_QUALITY) {
            quality *= 0.85; // thin material, slightly penalize
        }

        // 4. Recency — favor topics from roughly the last few days.
        double recency = recencyScore(topic.getPublishedAt());

        double total = WEIGHT_RELEVANCE * relevance + WEIGHT_QUALITY * quality + WEIGHT_RECENCY * recency;
        total = Math.round(total * 1000.0) / 1000.0;

        if (total < profile.getRejectionThreshold()) {
            return new Decision(topic, false,
                    String.format(Locale.ROOT,
                            "Below editorial bar (score %.2f < threshold %.2f) — relevant but not substantive or " +
                                    "timely enough to justify a post right now.",
                            total, profile.getRejectionThreshold()),
                    total, matched);
        }

        return new Decision(topic, true,
                "Clearly within persona domain, sufficiently substantive, and timely enough to be worth commentary.",
                total, matched);
    }

    /** Evaluate every candidate and pick the single strongest accepted one, if any. */
    public SelectionResult selectBest(List<Topic> candidates, PersonaProfile profile, AgentState agent) {
        List<Decision> decisions = new ArrayList<>();
        for (Topic t : candidates) {
            decisions.add(evaluate(t, profile, agent));
        }

        List<Decision> accepted = decisions.stream()
                .filter(Decision::isAccepted)
                .collect(Collectors.toList());

        List<Decision> rejected = decisions.stream()
                .filter(d -> !d.isAccepted())
                .collect(Collectors.toList());

        Decision best = accepted.stream()
                .max((a, b) -> Double.compare(a.getScore(), b.getScore()))
                .orElse(null);

        return new SelectionResult(best, decisions.size(), rejected.size(), accepted.size());
    }

    private boolean isTooSimilarToPublished(Topic topic, AgentState agent) {
        if (agent.getPublishedTopicKeys().contains(topic.key())) {
            return true; // exact repeat
        }
        Set<String> candidateTokens = tokenize(topic.getTitle());
        for (String publishedTitle : agent.getPublishedTitles()) {
            double sim = jaccard(candidateTokens, tokenize(publishedTitle));
            if (sim >= DUPLICATE_SIMILARITY_THRESHOLD) {
                return true;
            }
        }
        return false;
    }

    private List<String> matchedKeywords(Topic topic, PersonaProfile profile) {
        String haystack = ((topic.getTitle() == null ? "" : topic.getTitle()) + " "
                + (topic.getSummary() == null ? "" : topic.getSummary())).toLowerCase(Locale.ROOT);
        List<String> matches = new ArrayList<>();
        for (String kw : profile.getInterestKeywords()) {
            if (haystack.contains(kw.toLowerCase(Locale.ROOT))) {
                matches.add(kw);
            }
        }
        return matches;
    }

    private double recencyScore(Instant publishedAt) {
        if (publishedAt == null) return 0.5;
        long hours = Duration.between(publishedAt, Instant.now()).toHours();
        if (hours < 0) hours = 0;
        if (hours <= 24) return 1.0;
        if (hours <= 72) return 0.75;
        if (hours <= 168) return 0.5;
        return 0.25;
    }

    private Set<String> tokenize(String text) {
        if (text == null) return Set.of();
        String[] parts = text.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9\\s]", " ").split("\\s+");
        Set<String> tokens = new HashSet<>(Arrays.asList(parts));
        tokens.removeIf(t -> t.length() < 3);
        return tokens;
    }

    private double jaccard(Set<String> a, Set<String> b) {
        if (a.isEmpty() || b.isEmpty()) return 0.0;
        Set<String> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        Set<String> union = new HashSet<>(a);
        union.addAll(b);
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    /** Outcome of running editorial judgment over a full batch of candidates. */
    public static class SelectionResult {
        private final Decision chosen; // null if nothing was accepted
        private final int totalCandidates;
        private final int rejectedCount;
        private final int acceptedCount;

        public SelectionResult(Decision chosen, int totalCandidates, int rejectedCount, int acceptedCount) {
            this.chosen = chosen;
            this.totalCandidates = totalCandidates;
            this.rejectedCount = rejectedCount;
            this.acceptedCount = acceptedCount;
        }

        public Decision getChosen() {
            return chosen;
        }

        public int getTotalCandidates() {
            return totalCandidates;
        }

        public int getRejectedCount() {
            return rejectedCount;
        }

        public int getAcceptedCount() {
            return acceptedCount;
        }
    }
}
