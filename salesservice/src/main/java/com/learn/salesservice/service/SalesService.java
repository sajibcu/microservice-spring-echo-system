package com.learn.salesservice.service;

import com.learn.salesservice.dto.ProductDto;
import com.learn.salesservice.dto.SalesDto;
import com.learn.salesservice.dto.UserDto;
import com.learn.salesservice.model.Sales;
import com.learn.salesservice.repository.SalesRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class SalesService {

    private static final Logger log = LoggerFactory.getLogger(SalesService.class);

    private final SalesRepository salesRepository;
    private final WebClient webClient;

    private final ProductService productService;

    @Value("${app.product-service.url}")
    private String PRODUCTSERVICE_URL;

    @Value("${app.user-service.url}")
    private String USERSERVICE_URL;

    SalesService(SalesRepository salesRepository, WebClient.Builder webClientBuilder, ProductService productService) {
        this.salesRepository = salesRepository;
        this.webClient = webClientBuilder.build();
        this.productService = productService;
    }

    // Create a new Sales record
    public Sales createSales(Sales sales) {
        log.info("Creating sales record: {}", sales);
        return salesRepository.save(sales);
    }

    // Get all Sales records
    public List<SalesDto> getAllSales() {
        return salesRepository.findAll()
                .stream()
                .map(sales -> {
                    return SalesDto.builder()
                            .id( sales.getId() )
                            .product( this.productService.getProductById(sales.getProductId()).join() )
                            .salesTo(getUserByEmail(sales.getSalesTo()) )
                            .salesDate(sales.getSalesDate())
                            .build();

                    }
                )

                .toList();
    }

    // Read a Sales record by ID
    public SalesDto getSalesById(Long id) {
        log.info("Getting sales record with id: {}", id);
        return salesRepository.findById(id)
                .map(sales -> SalesDto.builder()
                        .id( sales.getId() )
                        .product( this.productService.getProductById(sales.getProductId()).join() )
                        .salesTo( getUserByEmail(sales.getSalesTo()) )
                        .salesDate(sales.getSalesDate())
                        .build())
                .orElseThrow(() -> new RuntimeException("Sales record not found with id: " + id));
    }

    // Delete a Sales record by ID
    public void deleteSales(Long id) {
        log.info("Deleting sales record with id: {}", id);
        salesRepository.deleteById(id);
    }

    private UserDto getUserByEmail(String email) {
        String userServiceUrl = USERSERVICE_URL + "/api/v1/user/email/" + email;
        log.info("userServiceUrl: {}", userServiceUrl);
        UserDto userDto =  webClient.get()
                .uri(userServiceUrl)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
        log.info("userDto: {}", userDto);
        return userDto;
    }

}
