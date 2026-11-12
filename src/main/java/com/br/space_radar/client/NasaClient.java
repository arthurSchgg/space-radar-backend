package com.br.space_radar.client;

import com.br.space_radar.dto.NasaFeedResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;

@Component
public class NasaClient {
    private final RestClient restClient;
    private final String apiKey;

    public NasaClient(RestClient.Builder builder, @Value("${nasa.api.key}") String apiKey) {
        this.restClient = builder.build();
        this.apiKey = apiKey;
    }

    public NasaFeedResponseDTO searchAsteroids(LocalDate startDate, LocalDate endDate) {
        try {
            return restClient.get()
                    .uri(uri -> uri.path("/neo/rest/v1/feed")
                            .queryParam("start_date", startDate)
                            .queryParam("end_date", endDate)
                            .queryParam("api_key", apiKey)
                            .build())
                    .retrieve().body(NasaFeedResponseDTO.class);
        } catch (RestClientException exception) {
            // Never include the upstream request URL: it contains the private NASA API key.
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "NASA feed is temporarily unavailable", exception);
        }
    }
}

