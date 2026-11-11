package com.br.space_radar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private final String[] allowedOrigins;

    public CorsConfig(@Value("${radar.cors.allowed-origins}") String origins) {
        this.allowedOrigins = Arrays.stream(origins.split(","))
                .map(String::trim).filter(s -> !s.isBlank()).toArray(String[]::new);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        for (String path : new String[] {"/v1/radar/**", "/v1/space-radar/**",
                "/v1/close-approach/**"}) {
            registry.addMapping(path).allowedOrigins(allowedOrigins)
                    .allowedMethods("GET").allowedHeaders("Accept", "Content-Type")
                    .maxAge(3600);
        }
        // Administrative writes are deliberately NOT browser-accessible via CORS.
    }
}

