package com.br.space_radar.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EstimatedDiameterDTO(

        @JsonProperty("meters")
        DiameterMetersDTO meters
) {
}
