package com.learn.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.productservice.model.Product;
import com.learn.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
public class ProductControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Product testProduct;
    private final Long productId = 1L;

    @BeforeEach
    void setUp() {

        testProduct = new Product();
        testProduct.setId(productId);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(99.99);
    }

    @Test
    public void updateProduct_ShouldNotUpdateProduct_WhenProductNotFound() throws Exception {
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

}
