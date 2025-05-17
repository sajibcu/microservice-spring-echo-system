package com.learn.productservice.controller;

import com.learn.productservice.model.Product;
import com.learn.productservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private Logger log = LoggerFactory.getLogger(ProductController.class);
    // add CRUD operations for product
    @Autowired
    private ProductService productService;

    // Create a new product
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    // Retrieve a product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) throws InterruptedException {
        log.info("Retrieving product with id: {}", id);
        long startTime = System.currentTimeMillis();
        if( startTime %2 == 0) {
            Thread.sleep(10000);
        }
        return productService.getProductById(id);
    }

    // Retrieve all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Update an existing product
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productService.updateProduct(id, updatedProduct);
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
