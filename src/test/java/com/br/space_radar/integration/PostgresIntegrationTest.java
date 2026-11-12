package com.br.space_radar.integration;

import com.br.space_radar.entity.Asteroid;
import com.br.space_radar.entity.CloseApproach;
import com.br.space_radar.repository.AsteroidRepository;
import com.br.space_radar.repository.CloseApproachRepository;
import com.br.space_radar.service.RadarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "DB_INTEGRATION_TEST", matches = "true")
class PostgresIntegrationTest {
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private AsteroidRepository asteroidRepository;
    @Autowired private CloseApproachRepository approachRepository;
    @Autowired private RadarService radarService;

    @Test
    void flywayCreatesSchemaAndIndexes() {
        Integer migrations = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM flyway_schema_history WHERE success = true", Integer.class);
        assertNotNull(migrations);
        assertTrue(migrations >= 2, "Both initial and index migrations should have been applied");

        Integer uniqueIndexes = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM pg_indexes WHERE tablename = 'tb_close_approach' " +
            "AND indexname = 'uk_approach_asteroid_date'", Integer.class);
        assertEquals(1, uniqueIndexes);
    }

    @Test
    @Transactional
    void readsFilteredEventsFromRealPostgres() {
        Asteroid asteroid = asteroidRepository.saveAndFlush(
            Asteroid.builder().nasaId(999999998L).name("CI-NASA-NEO")
               .dangerous(true).minDiameterM(15.0).maxDiameterM(23.0).build());
        var at = LocalDateTime.of(2026, 11, 12, 12, 30);
        CloseApproach approach = approachRepository.saveAndFlush(
            CloseApproach.builder().asteroid(asteroid).date(at)
                .distanceKm(480000.0).velocityKmS(17.2).lunarDistance(1.25).build());
        assertNotNull(approach.getId());

        var result = radarService.read(
           LocalDate.of(2026, 11, 11), LocalDate.of(2026, 11, 13),
           true, 1000000.0, 0, 10);
        assertTrue(result.events().content().stream()
            .anyMatch(x -> x.nasaId().equals(999999998L)));

        var excluded = radarService.read(
           LocalDate.of(2026, 11, 11), LocalDate.of(2026, 11, 13),
           true, 1000.0, 0, 10);
        assertTrue(excluded.events().content().stream()
            .noneMatch(x -> x.nasaId().equals(999999998L)));

        var byNasaId = asteroidRepository.findByNasaId(999999998L);
        assertTrue(byNasaId.isPresent());
        assertFalse(approachRepository.findByAsteroidId(
                asteroid.getId(), PageRequest.of(0, 10)).isEmpty());
    }
}
