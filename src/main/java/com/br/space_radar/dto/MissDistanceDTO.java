package com.br.space_radar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MissDistanceDTO(

        @JsonProperty("kilometers")
        Double kilometers,

        @JsonProperty("lunar")
        Double lunar
) {
}
