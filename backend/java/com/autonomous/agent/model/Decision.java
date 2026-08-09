package com.autonomous.agent.model;

import java.util.List;

/** The outcome of editorial judgment applied to one candidate topic. */
public class Decision {

    private final Topic topic;
    private final boolean accepted;
    private final String reason;
    private final double score;
    private final List<String> matchedKeywords;

    public Decision(Topic topic, boolean accepted, String reason, double score,
                     List<String> matchedKeywords) {
        this.topic = topic;
        this.accepted = accepted;
        this.reason = reason;
        this.score = score;
        this.matchedKeywords = matchedKeywords;
    }

    public Topic getTopic() {
        return topic;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getReason() {
        return reason;
    }

    public double getScore() {
        return score;
    }

    public List<String> getMatchedKeywords() {
        return matchedKeywords;
    }
}
