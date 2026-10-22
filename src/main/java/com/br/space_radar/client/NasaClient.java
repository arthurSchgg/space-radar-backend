package com.br.space_radar.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NasaClient {

    private final RestClient restClient;

    public NasaClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }
}
