package com.br.space_radar.mapper;

import com.br.space_radar.dto.*;
import com.br.space_radar.entity.Asteroid;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ApproachMapperTest {
    private final ApproachMapper mapper = new ApproachMapper();

    @Test
    void usesFullNasaTimestampNotMidnight() {
        var source = new CloseApproachNasaDTO("2026-11-11", "2026-Nov-11 15:42",
                new RelativeVelocityDTO(12.5),
                new MissDistanceDTO(384400.0, 1.0));
        var result = mapper.toEntity(source, new Asteroid());
        assertEquals(LocalDateTime.of(2026, 11, 11, 15, 42), result.getDate());
        assertEquals(12.5, result.getVelocityKmS());
        assertEquals(384400.0, result.getDistanceKm());
    }

    @Test
    void rejectsMissingFullTimestamp() {
        var source = new CloseApproachNasaDTO("2026-11-11", null,
                new RelativeVelocityDTO(10.0), new MissDistanceDTO(500000.0, 1.5));
        assertThrows(IllegalArgumentException.class,
                () -> mapper.toEntity(source, new Asteroid()));
    }
}

