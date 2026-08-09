package com.autonomous.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry point for the Autonomous AI & Technology Persona Agent.
 *
 * Once an agent is initialized via POST /api/agent/init, a background
 * scheduler (AutonomousAgentScheduler) drives the whole discover -> judge ->
 * write -> remember -> publish loop with no further human input.
 */
@SpringBootApplication
@EnableScheduling
public class AgentApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgentApplication.class, args);
    }
}
