package com.autonomous.agent.dto;

import java.util.List;

public class PostDto {

    private String id;
    private String createdAt; // ISO-8601 UTC
    private String text;
    private String rationale;
    private List<String> sources;

    public PostDto() {
    }

    public PostDto(String id, String createdAt, String text, String rationale, List<String> sources) {
        this.id = id;
        this.createdAt = createdAt;
        this.text = text;
        this.rationale = rationale;
        this.sources = sources;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }
}
