package com.example.app.controller;

import com.example.app.model.Product;
import com.example.app.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    private final ProductRepository productRepository;

    public TestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping
    public Product createTestProduct() {

        Product product = new Product(
                "Gaming Mouse",
                "RGB gaming mouse",
                299.99,
                "test-user"
        );

        return productRepository.save(product);
    }

    @GetMapping
    public Iterable<Product> getProducts() {
        return productRepository.findAll();
    }
}