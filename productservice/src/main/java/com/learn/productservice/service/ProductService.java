package com.learn.productservice.service;

import com.learn.productservice.model.Product;
import com.learn.productservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    private ProductRepository productRepository;

    // Create a new product
    public Product createProduct(Product product) {
        log.info("Creating a new product: {}", product);
        return productRepository.save(product);
    }

    // Retrieve a product by ID
    public Product getProductById(Long id) {
        log.info("Retrieving product with id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // Retrieve all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Update an existing product
    public Product updateProduct(Long id, Product updatedProduct) {
        log.info("Updating product with id: {}", id);
        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // Delete a product by ID
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        if (productRepository.existsById(id)) {
            log.info("Product found with id: {}, deleting...", id);
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    public void updateProductInventory(Long productId, int quantity) {
        log.info("Updating product quantity for product ID: {}, new quantity: {}", productId, quantity);
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            int productQuantity = product.getQuantity() == null ? 0 :  product.getQuantity();
            product.setQuantity(productQuantity + quantity);
            productRepository.save(product);
            log.info("Product quantity updated successfully for product ID: {}", productId);
        } else {
            log.error("Product not found with ID: {}", productId);
            throw new RuntimeException("Product not found with id: " + productId);
        }
    }
}
