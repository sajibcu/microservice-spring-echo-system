package com.learn.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.productservice.model.Product;
import com.learn.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private ProductService productService;
    
    @InjectMocks
    private ProductController productController;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Product testProduct;
    private final Long productId = 1L;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        
        testProduct = new Product();
        testProduct.setId(productId);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(99.99);
    }

    // Create Product Tests
    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(testProduct);

        mockMvc.perform(post("/api/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId.intValue())))
                .andExpect(jsonPath("$.name", is("Test Product")));

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    void createProduct_ShouldReturnBadRequest() throws Exception {
        Product invalidProduct = new Product();
        invalidProduct.setName(""); // Invalid product with empty name

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest());

        verify(productService, never()).createProduct(any(Product.class));
    }

    // Get Product By ID Tests
    @Test
    void getProductById_ShouldReturnProduct() throws Exception {
        when(productService.getProductById(anyLong())).thenReturn(testProduct);

        mockMvc.perform(get("/api/v1/product/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId.intValue())))
                .andExpect(jsonPath("$.name", is("Test Product")));

        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void getProductById_ShouldReturnNotFound() throws Exception {
        when(productService.getProductById(anyLong())).thenThrow(new RuntimeException("Product not found"));

        mockMvc.perform(get("/api/v1/product/{productId}", productId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Product not found", result.getResolvedException().getMessage()));

        verify(productService, times(1)).getProductById(productId);
    }

    // Get All Products Tests
    @Test
    void getAllProducts_ShouldReturnListOfProducts() throws Exception {
        List<Product> products = Arrays.asList(testProduct);
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/v1/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(productId.intValue())))
                .andExpect(jsonPath("$[0].name", is("Test Product")));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getAllProducts_ShouldReturnEmptyList() throws Exception {
        when(productService.getAllProducts()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(productService, times(1)).getAllProducts();
    }

    // Update Product Tests
    @Test
    void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(89.99);

        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/v1/product/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId.intValue())))
                .andExpect(jsonPath("$.name", is("Updated Product")));

        verify(productService, times(1)).updateProduct(eq(productId), any(Product.class));
    }

    @Test
    void updateProduct_ShouldReturnNotFound() throws Exception {
        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(89.99);

        when(productService.updateProduct(anyLong(), any(Product.class)))
                .thenThrow(new RuntimeException("Product not found with id: " + productId));

        mockMvc.perform(put("/api/v1/product/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isNotFound());
                //.andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                //.andExpect(result -> assertEquals("Product not found with id: " + productId, result.getResolvedException().getMessage()));

        verify(productService, times(1)).updateProduct(eq(productId), any(Product.class));
    }

    // Delete Product Tests
    @Test
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        doNothing().when(productService).deleteProduct(anyLong());

        mockMvc.perform(delete("/api/v1/product/{id}", productId))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    void deleteProduct_ShouldReturnNotFound() throws Exception {
        doThrow(new RuntimeException("Product not found")).when(productService).deleteProduct(anyLong());

        mockMvc.perform(delete("/api/v1/product/{id}", productId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Product not found", result.getResolvedException().getMessage()));

        verify(productService, times(1)).deleteProduct(productId);
    }
}