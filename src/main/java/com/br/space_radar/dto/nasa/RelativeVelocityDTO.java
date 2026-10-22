package com.br.space_radar.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RelativeVelocityDTO(

        @JsonProperty("kilometers_per_second")
        Double kilometetsPerSecond
) {
}
