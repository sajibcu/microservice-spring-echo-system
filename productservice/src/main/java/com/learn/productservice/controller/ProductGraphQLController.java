package com.learn.productservice.controller;

import com.learn.productservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.learn.productservice.model.Product;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductGraphQLController {

    private Logger log = LoggerFactory.getLogger(ProductController.class);
    // add CRUD operations for product
    @Autowired
    private ProductService productService;

    @QueryMapping
    public List<Product> products() {
        return productService.getAllProducts();
    }


}
