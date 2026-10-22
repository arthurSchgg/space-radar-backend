    package com.br.space_radar.mapper;

    import com.br.space_radar.dto.CloseApproachNasaDTO;
    import com.br.space_radar.entity.Asteroid;
    import com.br.space_radar.entity.CloseApproach;
    import org.springframework.stereotype.Component;

    import java.time.LocalDate;

    @Component
    public class ApproachMapper {

        public CloseApproach toEntity(CloseApproachNasaDTO dto, Asteroid asteroid){
            return CloseApproach.builder()
                    .asteroid(asteroid)
                    .date(LocalDate.parse(dto.date()).atStartOfDay())
                    .velocityKmS(dto.relativeVelocity().kilometetsPerSecond())
                    .distanceKm(dto.missDistance().kilometers())
                    .lunarDistance(dto.missDistance().lunar())
                    .build();
        }
    }
