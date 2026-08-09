package com.autonomous.agent.model;

import java.util.List;

/**
 * The internal, derived identity of an agent. Where {@link Persona} is just the
 * (name, domain) the caller supplied, PersonaProfile is the concrete editorial
 * personality built from it: what it cares about, how it talks, what it will
 * and won't publish. This is what keeps the voice consistent across every post.
 */
public class PersonaProfile {

    private final String name;
    private final String domain;
    private final String tagline;
    private final List<String> interestKeywords;
    private final List<String> openingHooks;
    private final List<String> opinionStances;   // templates containing {topic}
    private final List<String> closingSignoffs;
    private final List<String> hashtags;
    private final String arxivCategory;
    private final double rejectionThreshold;

    public PersonaProfile(String name, String domain, String tagline,
                           List<String> interestKeywords, List<String> openingHooks,
                           List<String> opinionStances, List<String> closingSignoffs,
                           List<String> hashtags, String arxivCategory,
                           double rejectionThreshold) {
        this.name = name;
        this.domain = domain;
        this.tagline = tagline;
        this.interestKeywords = interestKeywords;
        this.openingHooks = openingHooks;
        this.opinionStances = opinionStances;
        this.closingSignoffs = closingSignoffs;
        this.hashtags = hashtags;
        this.arxivCategory = arxivCategory;
        this.rejectionThreshold = rejectionThreshold;
    }

    public String getName() {
        return name;
    }

    public String getDomain() {
        return domain;
    }

    public String getTagline() {
        return tagline;
    }

    public List<String> getInterestKeywords() {
        return interestKeywords;
    }

    public List<String> getOpeningHooks() {
        return openingHooks;
    }

    public List<String> getOpinionStances() {
        return opinionStances;
    }

    public List<String> getClosingSignoffs() {
        return closingSignoffs;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public String getArxivCategory() {
        return arxivCategory;
    }

    public double getRejectionThreshold() {
        return rejectionThreshold;
    }
}
