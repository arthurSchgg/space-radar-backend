package com.br.space_radar.dto;

import java.time.LocalDate;

public record RadarResponseDTO(
        String visualizationMode, boolean realTimeCoordinatesAvailable,
        String distanceUnit, String velocityUnit,
        LocalDate startDate, LocalDate endDate,
        PageResponseDTO<ApproachResponseDTO> events
) {}

