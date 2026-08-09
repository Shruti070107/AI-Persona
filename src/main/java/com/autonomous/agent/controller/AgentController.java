package com.autonomous.agent.controller;

import com.autonomous.agent.dto.ErrorResponse;
import com.autonomous.agent.dto.FeedResponse;
import com.autonomous.agent.dto.InitRequest;
import com.autonomous.agent.dto.InitResponse;
import com.autonomous.agent.dto.PostDto;
import com.autonomous.agent.dto.StatusResponse;
import com.autonomous.agent.model.AgentState;
import com.autonomous.agent.model.Post;
import com.autonomous.agent.repository.AgentRepository;
import com.autonomous.agent.service.AgentOrchestrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AgentController {

    private final AgentOrchestrationService orchestrationService;
    private final AgentRepository repository;

    public AgentController(AgentOrchestrationService orchestrationService, AgentRepository repository) {
        this.orchestrationService = orchestrationService;
        this.repository = repository;
    }

    /**
     * Initializes the autonomous agent. Called exactly once before evaluation
     * begins. After this call returns, the agent operates entirely on its
     * own via the background scheduler.
     */
    @PostMapping("/api/agent/init")
    public ResponseEntity<?> init(@RequestBody(required = false) InitRequest request) {
        if (request == null || request.getPersona() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Request body must include a 'persona'."));
        }
        if (isBlank(request.getPersona().getName()) || isBlank(request.getPersona().getDomain())) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("'persona.name' and 'persona.domain' are both required."));
        }

        AgentState agent = orchestrationService.initializeAgent(request.getPersona());
        return ResponseEntity.ok(new InitResponse(agent.getId()));
    }

    /**
     * Retrieves the agent's feed. This is the only endpoint called after
     * initialization; new posts appear here over time as the agent publishes
     * autonomously.
     */
    @GetMapping("/api/agent/feed")
    public ResponseEntity<?> feed(@RequestParam("agentId") String agentId) {
        Optional<AgentState> maybeAgent = repository.findById(agentId);
        if (maybeAgent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No agent found for agentId: " + agentId));
        }

        AgentState agent = maybeAgent.get();
        List<PostDto> posts = agent.getPosts().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(this::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new FeedResponse(posts));
    }

    /**
     * Convenience endpoint for the bundled frontend (not part of the
     * required evaluation contract). Exposes persona identity and scheduling
     * status so the UI can show that the agent is genuinely operating on its
     * own clock, without exposing anything the two required endpoints don't
     * already imply.
     */
    @GetMapping("/api/agent/status")
    public ResponseEntity<?> status(@RequestParam("agentId") String agentId) {
        Optional<AgentState> maybeAgent = repository.findById(agentId);
        if (maybeAgent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No agent found for agentId: " + agentId));
        }
        AgentState agent = maybeAgent.get();
        String nextPublishAt = agent.getNextPublishAt() != null
                ? DateTimeFormatter.ISO_INSTANT.format(agent.getNextPublishAt())
                : null;

        return ResponseEntity.ok(new StatusResponse(
                agent.getId(),
                agent.getProfile().getName(),
                agent.getProfile().getDomain(),
                agent.getProfile().getTagline(),
                DateTimeFormatter.ISO_INSTANT.format(agent.getCreatedAt()),
                agent.getPosts().size(),
                nextPublishAt
        ));
    }

    private PostDto toDto(Post post) {
        return new PostDto(
                post.getId(),
                DateTimeFormatter.ISO_INSTANT.format(post.getCreatedAt()),
                post.getText(),
                post.getRationale(),
                post.getSources()
        );
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
