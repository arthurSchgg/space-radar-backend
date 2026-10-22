package com.br.space_radar.client;

import com.br.space_radar.dto.nasa.NasaFeedResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;

@Component
public class NasaClient {

    private final RestClient restClient;

    public NasaClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public NasaFeedResponseDTO searchAsteroids(
            LocalDate startDate,
            LocalDate endDate,
            String apiKey) {

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/neo/rest/v1/feed")
                        .queryParam("start_date", startDate)
                        .queryParam("end_date", endDate)
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .body(NasaFeedResponseDTO.class);
    }
}
