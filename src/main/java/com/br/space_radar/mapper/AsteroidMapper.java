package com.br.space_radar.mapper;

import com.br.space_radar.dto.nasa.AsteroidRequestNasaDTO;
import com.br.space_radar.entity.Asteroid;
import org.springframework.stereotype.Component;

@Component
public class AsteroidMapper {

    public Asteroid toEntity(AsteroidRequestNasaDTO dto){
        return Asteroid.builder()
                .nasaId(dto.nasaId())
                .name(dto.name())
                .minDiameterM(dto.estimatedDiameter().meters().min())
                .maxDiameterM(dto.estimatedDiameter().meters().max())
                .dangerous(dto.dangerous())
                .urlJpl(dto.nasaJplUrl())
                .build();
    }

    public void updateEntity(Asteroid asteroid, AsteroidRequestNasaDTO dto){
        asteroid.setNasaId(dto.nasaId());
        asteroid.setName(dto.name());
        asteroid.setMinDiameterM(dto.estimatedDiameter().meters().min());
        asteroid.setMaxDiameterM(dto.estimatedDiameter().meters().max());
        asteroid.setDangerous(dto.dangerous());
        asteroid.setUrlJpl(dto.nasaJplUrl());
    }
}
