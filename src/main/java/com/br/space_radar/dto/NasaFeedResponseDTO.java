package com.br.space_radar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class NasaFeedResponseDTO {

    @JsonProperty("near_earth_objects")
    private Map<String, List<AsteroidRequestNasaDTO>> nearEarthObjects;

    public Map<String, List<AsteroidRequestNasaDTO>> getNearEarthObjects() {
        return nearEarthObjects;
    }

    public void setNearEarthObjects(
            Map<String, List<AsteroidRequestNasaDTO>> nearEarthObjects) {
        this.nearEarthObjects = nearEarthObjects;
    }
}
