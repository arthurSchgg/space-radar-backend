package com.br.space_radar.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient.Builder restClientBuilder(@Value("${nasa.api.base-url}") String baseUrl){
        return RestClient.builder()
                .baseUrl(baseUrl);
    }
}
