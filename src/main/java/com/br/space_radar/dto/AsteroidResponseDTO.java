package com.br.space_radar.dto;

import com.br.space_radar.entity.Asteroid;
import java.util.UUID;

public record AsteroidResponseDTO(
        UUID id, Long nasaId, String name,
        Double minDiameterM, Double maxDiameterM,
        boolean hazardous, String nasaJplUrl
) {
    public static AsteroidResponseDTO from(Asteroid asteroid) {
        return new AsteroidResponseDTO(
                asteroid.getId(), asteroid.getNasaId(), asteroid.getName(),
                asteroid.getMinDiameterM(), asteroid.getMaxDiameterM(),
                asteroid.isDangerous(), asteroid.getUrlJpl());
    }
}

