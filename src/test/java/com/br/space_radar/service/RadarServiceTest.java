package com.br.space_radar.service;

import com.br.space_radar.entity.Asteroid;
import com.br.space_radar.entity.CloseApproach;
import com.br.space_radar.repository.CloseApproachRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RadarServiceTest {
    private final CloseApproachRepository repository = mock(CloseApproachRepository.class);
    private final RadarService service = new RadarService(repository);

    @Test
    void returnsSchematicMetadataAndUtcApproach() {
        var asteroid = Asteroid.builder().id(UUID.randomUUID()).nasaId(42L)
                .name("Test object").dangerous(true).build();
        var approach = CloseApproach.builder().id(UUID.randomUUID()).asteroid(asteroid)
                .date(LocalDateTime.of(2026, 11, 11, 15, 42))
                .distanceKm(123456.0).velocityKmS(12.0).build();
        when(repository.findRadarEvents(any(), any(), eq(false), isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(approach)));
        var result = service.read(LocalDate.of(2026, 11, 11),
                LocalDate.of(2026, 11, 18), false, null, 0, 50);
        assertFalse(result.realTimeCoordinatesAvailable());
        assertEquals("SCHEMATIC_DISTANCE_ONLY", result.visualizationMode());
        assertEquals(1, result.events().totalElements());
        assertEquals(42L, result.events().content().getFirst().nasaId());
        assertEquals("Z", result.events().content().getFirst()
                .approachAtUtc().getOffset().getId());
    }

    @Test
    void rejectsInvalidDateRangeAndDistance() {
        var start = LocalDate.of(2026, 11, 11);
        assertThrows(ResponseStatusException.class,
                () -> service.read(start, start.minusDays(1), false, null, 0, 50));
        assertThrows(ResponseStatusException.class,
                () -> service.read(start, start, false, -1.0, 0, 50));
        assertThrows(ResponseStatusException.class,
                () -> service.read(start, start, false, null, 0, 101));
        verifyNoInteractions(repository);
    }
}

