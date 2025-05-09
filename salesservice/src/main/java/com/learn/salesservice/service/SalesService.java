package com.learn.salesservice.service;

import com.learn.salesservice.dto.ProductDto;
import com.learn.salesservice.dto.SalesDto;
import com.learn.salesservice.model.Sales;
import com.learn.salesservice.repository.SalesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
public class SalesService {

    private final SalesRepository salesRepository;
    private final WebClient webClient;

    @Value("${app.product-service.url}")
    private String PRODUCTSERVICE_URL;

    SalesService(SalesRepository salesRepository, WebClient.Builder webClientBuilder) {
        this.salesRepository = salesRepository;
        this.webClient = webClientBuilder.build();
    }

    // Create a new Sales record
    public Sales createSales(Sales sales) {
        return salesRepository.save(sales);
    }

    // Get all Sales records
    public List<SalesDto> getAllSales() {
        return salesRepository.findAll()
                .stream()
                .map(sales -> {
                    return SalesDto.builder()
                            .id( sales.getId() )
                            .product( getProductById(sales.getProductId()) )
                            .salesTo(sales.getSalesTo() )
                            .salesDate(sales.getSalesDate())
                            .build();

                    }
                )

                .toList();
    }

    // Read a Sales record by ID
    public SalesDto getSalesById(Long id) {
        return salesRepository.findById(id)
                .map(sales -> {
                    return SalesDto.builder()
                            .id( sales.getId() )
                            .product( getProductById(sales.getProductId()) )
                            .salesTo(sales.getSalesTo() )
                            .salesDate(sales.getSalesDate())
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Sales record not found with id: " + id));
    }

    // Delete a Sales record by ID
    public void deleteSales(Long id) {
        salesRepository.deleteById(id);
    }

    private ProductDto getProductById(Long productId) {
        String productServiceUrl = PRODUCTSERVICE_URL + "/api/v1/product/" + productId;
        ProductDto productDto =  webClient.get()
                .uri(productServiceUrl)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
        log.info("productDto: {}", productDto);
        return productDto;
    }
}
