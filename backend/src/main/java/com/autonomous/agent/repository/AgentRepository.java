package com.autonomous.agent.repository;

import com.autonomous.agent.model.AgentState;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory store for agent state. Simulated publishing does not require
 * durable storage; every agent lives for the lifetime of the JVM process,
 * which is sufficient for the ~48h observation window.
 */
@Repository
public class AgentRepository {

    private final Map<String, AgentState> agents = new ConcurrentHashMap<>();

    public AgentState save(AgentState agent) {
        agents.put(agent.getId(), agent);
        return agent;
    }

    public Optional<AgentState> findById(String id) {
        return Optional.ofNullable(agents.get(id));
    }

    public Collection<AgentState> findAll() {
        return agents.values();
    }
}
