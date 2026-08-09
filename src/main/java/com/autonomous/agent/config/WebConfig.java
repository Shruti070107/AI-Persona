package com.autonomous.agent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The bundled frontend is served from the same origin as the API (Spring
 * Boot serves it directly from src/main/resources/static), so this isn't
 * required for the default setup. It's here so the same backend also works
 * if the frontend is ever split out and hosted separately (e.g. a static
 * host or a different port during development).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*");
    }
}
