package com.br.space_radar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CloseApproachNasaDTO(
        @JsonProperty("close_approach_date") String date,
        @JsonProperty("close_approach_date_full") String dateFull,
        @JsonProperty("relative_velocity") RelativeVelocityDTO relativeVelocity,
        @JsonProperty("miss_distance") MissDistanceDTO missDistance
) {}

