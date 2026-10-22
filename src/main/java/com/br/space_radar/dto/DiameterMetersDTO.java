package com.br.space_radar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DiameterMetersDTO(
        @JsonProperty("estimated_diameter_min")
        Double min,

        @JsonProperty("estimated_diameter_max")
        Double max
) {
}
