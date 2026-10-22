package com.br.space_radar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RelativeVelocityDTO(

        @JsonProperty("kilometers_per_second")
        Double kilometetsPerSecond
) {
}
