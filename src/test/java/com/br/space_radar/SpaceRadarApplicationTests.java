package com.br.space_radar;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

// The Spring context requires PostgreSQL; unit tests must run without external services.
class SpaceRadarApplicationTests {
    @Test
    void applicationClassExists() {
        assertTrue(SpaceRadarApplication.class.isAnnotationPresent(
                org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }
}

