package com.br.space_radar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EstimatedDiameterDTO(

        @JsonProperty("meters")
        DiameterMetersDTO meters
) {
}
