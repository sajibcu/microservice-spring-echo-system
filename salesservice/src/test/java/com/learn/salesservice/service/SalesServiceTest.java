package com.learn.salesservice.service;

import com.learn.salesservice.dto.ProductDto;
import com.learn.salesservice.dto.SalesDto;
import com.learn.salesservice.dto.UserDto;
import com.learn.salesservice.model.Sales;
import com.learn.salesservice.repository.SalesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesServiceTest {

    @Mock
    private SalesRepository salesRepository;

    @Mock
    private ProductService productService;

    @Mock
    private SalesPublisher salesPublisher;

    private WebClient.Builder webClientBuilder;

    private WebClient webClient;

    private SalesService salesService;

    private Sales testSale;
    private ProductDto testProduct;
    private UserDto testUser;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        // Initialize the service with the mocked dependencies

        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        webClient = mock(WebClient.class);
        webClientBuilder = mock(WebClient.Builder.class);
        when(webClientBuilder.build()).thenReturn(webClient);



        lenient().when(webClient.get()).thenReturn(uriSpec);
        lenient().when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        lenient().when(headersSpec.retrieve()).thenReturn(responseSpec);

        UserDto mockUser = UserDto.builder()
                .id(1L)
                .name("Test User")
                .email("test@gmail.com")
                .phone("1234567890")
                .address("123 Test St, Test City")
                .build();

        lenient().when(responseSpec.bodyToMono(UserDto.class)).thenReturn(Mono.just(mockUser));


        salesService = new SalesService(salesRepository, webClientBuilder, productService, salesPublisher);
        
        // Setup test data
        testSale = new Sales();
        testSale.setId(1L);
        testSale.setProductId(101L);
        testSale.setSalesTo("test@example.com");
        testSale.setSalesDate(LocalDate.now().atStartOfDay());

        testProduct = ProductDto.builder()
                .id(101L)
                .name("Test Product")
                .price(99.99)
                .quantity(10)
                .build();

        testUser = UserDto.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
    }

    @Test
    void testCreateSales() {
        
        when(salesRepository.save(any(Sales.class))).thenReturn(testSale);
        
        Sales result = salesService.createSales(testSale);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(101L, result.getProductId());
        assertEquals("test@example.com", result.getSalesTo());
        verify(salesRepository, times(1)).save(testSale);
        verify(salesPublisher, times(1)).publishProductSold(101L, 1);
    }

    @Test
    void testGetAllSales() {
        when(salesRepository.findAll()).thenReturn(List.of(testSale));
        when(productService.getProductById(101L)).thenReturn(CompletableFuture.completedFuture(testProduct));

        List<SalesDto> result = salesService.getAllSales();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Test Product", result.get(0).getProduct().getName());
        assertEquals("Test User", result.get(0).getSalesTo().getName());
        verify(salesRepository, times(1)).findAll();
        verify(productService, times(1)).getProductById(101L);
    }


    @Test
    void testGetSalesById_success() {
        when(salesRepository.findById(1L)).thenReturn(Optional.of(testSale));
        when(productService.getProductById(101L)).thenReturn(CompletableFuture.completedFuture(testProduct));

        SalesDto result = salesService.getSalesById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getProduct().getName());
        assertEquals("Test User", result.getSalesTo().getName());
        verify(salesRepository, times(1)).findById(1L);
        verify(productService, times(1)).getProductById(101L);
    }

    @Test
    void testGetSalesById_notFound() {
        // Arrange
        when(salesRepository.findById(100L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            salesService.getSalesById(100L);
        });

        assertEquals(exception.getMessage(), "Sales record not found with id: 100");
        verify(salesRepository, times(1)).findById(100L);
    }
}
