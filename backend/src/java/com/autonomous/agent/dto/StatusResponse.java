package com.autonomous.agent.dto;

public class StatusResponse {

    private String agentId;
    private String name;
    private String domain;
    private String tagline;
    private String createdAt;
    private int postCount;
    private String nextPublishAt; // ISO-8601 UTC, informational only

    public StatusResponse() {
    }

    public StatusResponse(String agentId, String name, String domain, String tagline,
                           String createdAt, int postCount, String nextPublishAt) {
        this.agentId = agentId;
        this.name = name;
        this.domain = domain;
        this.tagline = tagline;
        this.createdAt = createdAt;
        this.postCount = postCount;
        this.nextPublishAt = nextPublishAt;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public String getNextPublishAt() {
        return nextPublishAt;
    }

    public void setNextPublishAt(String nextPublishAt) {
        this.nextPublishAt = nextPublishAt;
    }
}
