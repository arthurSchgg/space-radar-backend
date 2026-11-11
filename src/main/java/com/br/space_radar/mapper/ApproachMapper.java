package com.br.space_radar.mapper;

import com.br.space_radar.dto.CloseApproachNasaDTO;
import com.br.space_radar.entity.Asteroid;
import com.br.space_radar.entity.CloseApproach;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class ApproachMapper {
    private static final DateTimeFormatter NASA_DATE_TIME =
            DateTimeFormatter.ofPattern("uuuu-MMM-dd HH:mm", Locale.ENGLISH);

    public CloseApproach toEntity(CloseApproachNasaDTO dto, Asteroid asteroid) {
        if (dto == null || dto.dateFull() == null || dto.relativeVelocity() == null
                || dto.missDistance() == null) {
            throw new IllegalArgumentException("NASA returned incomplete close approach data");
        }
        return CloseApproach.builder()
                .asteroid(asteroid)
                .date(LocalDateTime.parse(dto.dateFull(), NASA_DATE_TIME))
                .velocityKmS(dto.relativeVelocity().kilometetsPerSecond())
                .distanceKm(dto.missDistance().kilometers())
                .lunarDistance(dto.missDistance().lunar())
                .build();
    }
}

