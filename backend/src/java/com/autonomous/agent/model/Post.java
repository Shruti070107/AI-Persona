package com.autonomous.agent.model;

import java.time.Instant;
import java.util.List;

/** A single published post, including the rationale that justifies it. */
public class Post {

    private final String id;
    private final Instant createdAt;
    private final String text;
    private final String rationale;
    private final List<String> sources;
    private final String topicKey; // internal, used for memory/continuity - not exposed in DTO directly

    public Post(String id, Instant createdAt, String text, String rationale,
                List<String> sources, String topicKey) {
        this.id = id;
        this.createdAt = createdAt;
        this.text = text;
        this.rationale = rationale;
        this.sources = sources;
        this.topicKey = topicKey;
    }

    public String getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public String getRationale() {
        return rationale;
    }

    public List<String> getSources() {
        return sources;
    }

    public String getTopicKey() {
        return topicKey;
    }
}
