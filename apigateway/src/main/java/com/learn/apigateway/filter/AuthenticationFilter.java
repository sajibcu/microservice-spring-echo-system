package com.learn.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);


    private final RouteValidator validator;

    private final WebClient webClient;

    @Value("${app.auth-service.url}")
    private String AUTH_SERVICE_URL;

    public AuthenticationFilter(RouteValidator validator, WebClient.Builder builder) {
        super(Config.class);
        this.validator = validator;
        this.webClient = builder.build();
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            if(validator.isSecured.test( request)) {
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing Authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                log.info("Authorization header: {}", authHeader);
                if(authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                log.info("Auth Service URL: {}", AUTH_SERVICE_URL);
                String authServiceUrl = AUTH_SERVICE_URL + "/api/v1/auth/validate?token=" + authHeader;
                log.info("Auth Service URL with token: {}", authServiceUrl);

                URI uri = URI.create(authServiceUrl);

                return webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(String.class)
                        .flatMap(response -> {
                            log.info("Response from Auth Service: {}", response);
                            if (!"Token is valid".equals(response)) {
                                return Mono.error(new RuntimeException("Invalid token"));
                            }
                            return chain.filter(exchange);
                        })
                        .onErrorResume(e -> {
                            log.error("Error validating token: {}", e.getMessage());
                            return Mono.error(new RuntimeException("Invalid token"));
                        });

            }

            log.info("Request url: {}", exchange.getRequest().getURI());

            return chain.filter(exchange);
        };
    }

    public static class Config {

    }

}
