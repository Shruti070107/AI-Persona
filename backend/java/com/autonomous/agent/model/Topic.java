package com.autonomous.agent.model;

import java.time.Instant;

/** A raw candidate topic discovered from a live information source. */
public class Topic {

    private String title;
    private String summary;
    private String url;
    private String secondarySourceUrl; // e.g. HN discussion thread
    private String source;             // "Hacker News" | "arXiv"
    private Instant publishedAt;
    private double signal;             // normalized popularity/quality indicator, 0-1

    public Topic() {
    }

    public Topic(String title, String summary, String url, String secondarySourceUrl,
                 String source, Instant publishedAt, double signal) {
        this.title = title;
        this.summary = summary;
        this.url = url;
        this.secondarySourceUrl = secondarySourceUrl;
        this.source = source;
        this.publishedAt = publishedAt;
        this.signal = signal;
    }

    /** Normalized key used for de-duplication / memory lookups. */
    public String key() {
        return title == null ? "" : title.trim().toLowerCase();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecondarySourceUrl() {
        return secondarySourceUrl;
    }

    public void setSecondarySourceUrl(String secondarySourceUrl) {
        this.secondarySourceUrl = secondarySourceUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public double getSignal() {
        return signal;
    }

    public void setSignal(double signal) {
        this.signal = signal;
    }
}
