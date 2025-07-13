package com.learn.productservice.service;

import com.learn.productservice.model.Product;
import com.learn.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    public void setUp() {
        // Initialize any necessary mocks or setup before each test
        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("This is a test product")
                .price(99.99)
                .quantity(10)
                .addedBy(1L)
                .build();
    }

    @Test
    public void testCreateProduct() {
        // Implement test logic for creating a product
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        assertEquals(1L, createdProduct.getId());

        assertEquals("Test Product", createdProduct.getName());
        assertEquals("This is a test product", createdProduct.getDescription());
        assertEquals(99.99, createdProduct.getPrice());
        assertEquals(10, createdProduct.getQuantity());
        assertEquals(1L, createdProduct.getAddedBy());

    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetProductById_Success() {
        // Implement test logic for retrieving a product by ID
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertEquals(1L, foundProduct.getId());
        assertEquals("Test Product", foundProduct.getName());
        assertEquals("This is a test product", foundProduct.getDescription());
        assertEquals(99.99, foundProduct.getPrice());
        assertEquals(10, foundProduct.getQuantity());
        assertEquals(1L, foundProduct.getAddedBy());
    }

    @Test
    public void testGetProductById_NotFound() {
        // Implement test logic for retrieving a product by ID when not found
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        try {
            productService.getProductById(1L);
        } catch (RuntimeException e) {
            assertEquals("Product not found with id: 1", e.getMessage());
        }
    }

    @Test
    public void testUpdateProduct_Success() {
        // Implement test logic for updating a product
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = Product.builder()
                .name("Updated Product")
                .description("This is an updated product")
                .price(89.99)
                .quantity(5)
                .addedBy(1L)
                .build();

        Product result = productService.updateProduct(1L, updatedProduct);

        assertEquals(1L, result.getId());
        assertEquals("Updated Product", result.getName());
        assertEquals("This is an updated product", result.getDescription());
        assertEquals(89.99, result.getPrice());
        assertEquals(5, result.getQuantity());
        assertEquals(1L, result.getAddedBy());
    }

    @Test
    public void testUpdateProduct_NotFound() {
        // Implement test logic for updating a product when not found
        when(productRepository.findById(100L)).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(100L, product);
        });
        assertEquals("Product not found with id: 100", exception.getMessage());
    }

    @Test
    public void testDeleteProduct_Success() {
        // Implement test logic for deleting a product
        when(productRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> productService.deleteProduct(1L));

        verify(productRepository).deleteById(1L);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        // Implement test logic for deleting a product when not found
        when(productRepository.existsById(100L)).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(100L);
        });
        assertEquals("Product not found with id: 100", exception.getMessage());
    }

    @Test
    void testUpdateProductInventorySuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.updateProductInventory(1L, 5);
        assertEquals(15, product.getQuantity());
    }

    @Test
    void testUpdateProductInventoryProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productService.updateProductInventory(1L, 5));
        assertEquals("Product not found with id: 1", exception.getMessage());
    }
}
