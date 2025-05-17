package com.learn.salesservice.service;

import com.learn.salesservice.dto.ProductDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);


    private final WebClient webClient;

    @Value("${app.product-service.url}")
    private String PRODUCTSERVICE_URL;

    ProductService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "getProductByIdFallback")
    @TimeLimiter(name = "productService")
    public CompletableFuture<ProductDto> getProductById(Long productId) {
        String productServiceUrl = PRODUCTSERVICE_URL + "/api/v1/product/" + productId;
        log.info("productServiceUrl: {}", productServiceUrl);
        return webClient.get()
                .uri(productServiceUrl)
                .retrieve()
                .onStatus(
                        status -> status.is5xxServerError() || status.is4xxClientError(),
                        clientResponse -> {
                            log.error("Error response from PRODUCTSERVICE: {}", clientResponse.statusCode());
                            return Mono.error(new RuntimeException("Service unavailable"));
                        }
                )
                .bodyToMono(ProductDto.class)
                .doOnError(error -> log.error("Error occurred while calling PRODUCTSERVICE", error))
                .toFuture();
    }

    public CompletableFuture<ProductDto> getProductByIdFallback(Long productId, Throwable t) {
        log.error("Error occurred while fetching product with id: {}. Fallback method called.", productId, t);
        return CompletableFuture.completedFuture(
                ProductDto.builder()
                        .id(productId)
                        .name("Fallback Product")
                        .description("Fallback description")
                        .price(0.0)
                        .quantity(0)
                        .build()
        );
    }

}
