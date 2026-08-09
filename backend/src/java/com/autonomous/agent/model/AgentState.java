package com.autonomous.agent.model;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The full runtime state of one autonomous agent: its identity, its memory of
 * everything it has ever published (so it doesn't repeat itself), and the
 * scheduling state that drives autonomous publishing over time.
 */
public class AgentState {

    private final String id;
    private final Persona requestedPersona;
    private final PersonaProfile profile;
    private final Instant createdAt;

    private final CopyOnWriteArrayList<Post> posts = new CopyOnWriteArrayList<>();

    // Memory: normalized titles/topic-keys of everything ever published, used
    // to avoid repetition and to reason about novelty of new candidates.
    private final Set<String> publishedTopicKeys = ConcurrentHashMap.newKeySet();
    private final CopyOnWriteArrayList<String> publishedTitles = new CopyOnWriteArrayList<>();

    private volatile Instant nextPublishAt;
    private final ReentrantLock cycleLock = new ReentrantLock();
    private final AtomicInteger cycleCount = new AtomicInteger(0);
    private final AtomicInteger postIdSequence = new AtomicInteger(0);

    public AgentState(String id, Persona requestedPersona, PersonaProfile profile, Instant createdAt) {
        this.id = id;
        this.requestedPersona = requestedPersona;
        this.profile = profile;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public Persona getRequestedPersona() {
        return requestedPersona;
    }

    public PersonaProfile getProfile() {
        return profile;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public Set<String> getPublishedTopicKeys() {
        return publishedTopicKeys;
    }

    public List<String> getPublishedTitles() {
        return publishedTitles;
    }

    public void remember(Topic topic) {
        publishedTopicKeys.add(topic.key());
        publishedTitles.add(topic.getTitle());
    }

    public Instant getNextPublishAt() {
        return nextPublishAt;
    }

    public void setNextPublishAt(Instant nextPublishAt) {
        this.nextPublishAt = nextPublishAt;
    }

    public ReentrantLock getCycleLock() {
        return cycleLock;
    }

    public int nextCycle() {
        return cycleCount.incrementAndGet();
    }

    public String nextPostId() {
        return "p" + postIdSequence.incrementAndGet();
    }
}
