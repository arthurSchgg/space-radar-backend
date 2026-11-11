package com.br.space_radar.dto;

import java.time.LocalDate;

public record ImportSummaryDTO(
        LocalDate startDate, LocalDate endDate,
        int asteroidsCreated, int asteroidsUpdated,
        int approachesCreated, int approachesUpdated
) {}

