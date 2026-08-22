package com.example.app.controller;

import org.springframework.web.bind.annotation.*;
import com.example.app.repository.ProductRepository;
import com.example.app.model.Product;

@RestController
@RequestMapping("/products")
public class ProductsController {
    
    private ProductRepository productRepository;

    public ProductsController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping()
    public Product postProducts(@RequestBody Product product) {
        Product p = new Product(product.getName(), product.getDescription(), product.getPrice(), product.getUserId());
        return productRepository.save(p);
    }
}
