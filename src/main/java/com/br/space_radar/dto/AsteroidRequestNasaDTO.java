package com.br.space_radar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AsteroidRequestNasaDTO(
        @JsonProperty("id")
        Long nasaId,

        String name,

        @JsonProperty("estimated_diameter")
        EstimatedDiameterDTO estimatedDiameter,

        @JsonProperty("is_potentially_hazardous_asteroid")
        boolean dangerous,

        @JsonProperty("nasa_jpl_url")
        String nasaJplUrl,

        @JsonProperty("close_approach_data")
        List<CloseApproachNasaDTO> closeApproachData
) {
}
