package com.autonomous.agent.service;

import com.autonomous.agent.model.AgentState;
import com.autonomous.agent.repository.AgentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * The heartbeat of autonomy. Ticks on a fixed interval and, for every
 * initialized agent whose scheduled publish time has arrived, triggers a full
 * discover -> judge -> write -> remember cycle. This is what allows posts to
 * keep appearing in the feed over the ~48h observation window without any
 * further calls from the evaluator.
 */
@Component
public class AutonomousAgentScheduler {

    private static final Logger log = LoggerFactory.getLogger(AutonomousAgentScheduler.class);

    private final AgentRepository repository;
    private final AgentOrchestrationService orchestrationService;

    public AutonomousAgentScheduler(AgentRepository repository, AgentOrchestrationService orchestrationService) {
        this.repository = repository;
        this.orchestrationService = orchestrationService;
    }

    @Scheduled(fixedDelayString = "${agent.scheduler.tick-ms:60000}")
    public void tick() {
        Instant now = Instant.now();
        for (AgentState agent : repository.findAll()) {
            if (agent.getNextPublishAt() != null && !now.isBefore(agent.getNextPublishAt())) {
                try {
                    orchestrationService.runCycle(agent);
                } catch (Exception e) {
                    log.error("Unexpected error running cycle for agent {}: {}", agent.getId(), e.getMessage(), e);
                }
            }
        }
    }
}
