package com.br.space_radar.dto;

import com.br.space_radar.entity.CloseApproach;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public record ApproachResponseDTO(
        UUID id, UUID asteroidId, Long nasaId, String asteroidName,
        boolean hazardous, OffsetDateTime approachAtUtc,
        Double velocityKmS, Double distanceKm, Double lunarDistance
) {
    public static ApproachResponseDTO from(CloseApproach approach) {
        var asteroid = approach.getAsteroid();
        return new ApproachResponseDTO(
                approach.getId(), asteroid.getId(), asteroid.getNasaId(),
                asteroid.getName(), asteroid.isDangerous(),
                approach.getDate().atOffset(ZoneOffset.UTC),
                approach.getVelocityKmS(), approach.getDistanceKm(),
                approach.getLunarDistance());
    }
}

