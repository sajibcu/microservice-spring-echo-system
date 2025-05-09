package com.learn.salesservice.config;

import org.slf4j.MDC;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    @LoadBalanced
    @Profile("!local")
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filter( (request, next) -> {
                    String correlationId = MDC.get("correlationId");
                    if (correlationId != null) {
                        return next.exchange(
                                ClientRequest.from(request)
                                        .header("X-Correlation-Id", correlationId)
                                        .build()
                        );
                    }
                    return next.exchange(request);
                });
    }

    @Bean
    @Profile("local")
    public WebClient.Builder localWebClientBuilder() {
        return WebClient.builder();
    }
}
