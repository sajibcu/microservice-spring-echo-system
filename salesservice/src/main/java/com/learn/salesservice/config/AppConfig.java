package com.learn.salesservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    @LoadBalanced
    @Profile("!local")
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @Profile("local")
    public WebClient.Builder localWebClientBuilder() {
        return WebClient.builder();
    }
}
