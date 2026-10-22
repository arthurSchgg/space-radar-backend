package com.br.space_radar.client;

import com.br.space_radar.dto.NasaFeedResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;

@Component
public class NasaClient {

    private final RestClient restClient;
    private final String apiKey;

    public NasaClient(RestClient.Builder restClientBuilder, @Value("${nasa.api.key}") String apiKey) {
        this.restClient = restClientBuilder.build();
        this.apiKey = apiKey;
    }

    public NasaFeedResponseDTO searchAsteroids(
            LocalDate startDate,
            LocalDate endDate) {

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
