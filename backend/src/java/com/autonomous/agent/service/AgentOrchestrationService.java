package com.autonomous.agent.service;

import com.autonomous.agent.model.AgentState;
import com.autonomous.agent.model.Decision;
import com.autonomous.agent.model.Persona;
import com.autonomous.agent.model.PersonaProfile;
import com.autonomous.agent.model.Post;
import com.autonomous.agent.model.Topic;
import com.autonomous.agent.repository.AgentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Orchestrates the end-to-end autonomous loop for a single agent:
 * discover live topics -> apply editorial judgment -> write in persona voice
 * -> remember what was published -> schedule the next cycle.
 *
 * This is invoked once at initialization (to schedule the first cycle) and
 * then repeatedly by {@link AutonomousAgentScheduler} with no further human
 * input.
 */
@Service
public class AgentOrchestrationService {

    private static final Logger log = LoggerFactory.getLogger(AgentOrchestrationService.class);

    private final AgentRepository repository;
    private final PersonaProfileFactory profileFactory;
    private final TopicDiscoveryService discoveryService;
    private final EditorialJudgmentService judgmentService;
    private final ContentGenerationService contentService;

    @Value("${agent.scheduler.initial-delay-seconds:45}")
    private int initialDelaySeconds;

    @Value("${agent.scheduler.retry-interval-minutes:12}")
    private int retryIntervalMinutes;

    @Value("${agent.scheduler.min-interval-minutes:55}")
    private int minIntervalMinutes;

    @Value("${agent.scheduler.max-interval-minutes:140}")
    private int maxIntervalMinutes;

    public AgentOrchestrationService(AgentRepository repository,
                                      PersonaProfileFactory profileFactory,
                                      TopicDiscoveryService discoveryService,
                                      EditorialJudgmentService judgmentService,
                                      ContentGenerationService contentService) {
        this.repository = repository;
        this.profileFactory = profileFactory;
        this.discoveryService = discoveryService;
        this.judgmentService = judgmentService;
        this.contentService = contentService;
    }

    /** Called exactly once, from POST /api/agent/init. */
    public AgentState initializeAgent(Persona persona) {
        PersonaProfile profile = profileFactory.build(persona);
        String agentId = UUID.randomUUID().toString();
        AgentState agent = new AgentState(agentId, persona, profile, Instant.now());
        agent.setNextPublishAt(Instant.now().plusSeconds(initialDelaySeconds));
        repository.save(agent);
        log.info("Initialized agent {} as persona '{}' ({}). First publishing cycle scheduled at {}.",
                agentId, profile.getName(), profile.getDomain(), agent.getNextPublishAt());
        return agent;
    }

    /**
     * Runs a single autonomous publishing cycle for the given agent. Safe to
     * call repeatedly; uses a per-agent lock so overlapping scheduler ticks
     * never run two cycles for the same agent concurrently.
     */
    public void runCycle(AgentState agent) {
        ReentrantLock lock = agent.getCycleLock();
        if (!lock.tryLock()) {
            return; // a cycle is already in progress for this agent
        }
        try {
            int cycle = agent.nextCycle();
            PersonaProfile profile = agent.getProfile();
            log.info("Agent {} ({}) starting cycle #{}", agent.getId(), profile.getName(), cycle);

            List<Topic> candidates = discoveryService.discoverAll(profile);
            log.info("Agent {} discovered {} candidate topics", agent.getId(), candidates.size());

            EditorialJudgmentService.SelectionResult result =
                    judgmentService.selectBest(candidates, profile, agent);

            if (result.getChosen() == null) {
                log.info("Agent {} found nothing worth publishing this cycle ({} candidates, {} accepted). " +
                                "Retrying sooner.", agent.getId(), result.getTotalCandidates(), result.getAcceptedCount());
                agent.setNextPublishAt(Instant.now().plus(retryIntervalMinutes, ChronoUnit.MINUTES));
                return;
            }

            Decision decision = result.getChosen();
            Post post = contentService.generate(
                    profile, decision, result.getTotalCandidates(), result.getAcceptedCount(), agent.nextPostId());

            agent.addPost(post);
            agent.remember(decision.getTopic());

            log.info("Agent {} published post {} on topic '{}' (score {})",
                    agent.getId(), post.getId(), decision.getTopic().getTitle(), decision.getScore());

            int spacingMinutes = ThreadLocalRandom.current().nextInt(minIntervalMinutes, maxIntervalMinutes + 1);
            agent.setNextPublishAt(Instant.now().plus(spacingMinutes, ChronoUnit.MINUTES));
        } catch (Exception e) {
            log.error("Agent {} cycle failed: {}", agent.getId(), e.getMessage(), e);
            // Back off and try again later rather than spinning on a persistent error.
            agent.setNextPublishAt(Instant.now().plus(retryIntervalMinutes, ChronoUnit.MINUTES));
        } finally {
            lock.unlock();
        }
    }
}
