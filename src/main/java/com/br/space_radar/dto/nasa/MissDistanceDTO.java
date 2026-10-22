package com.br.space_radar.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MissDistanceDTO(

        @JsonProperty("kilometers")
        Double kilometers,

        @JsonProperty("lunar")
        Double lunar
) {
}
