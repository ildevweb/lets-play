package com.example.app.controller;

import com.example.app.dto.ProductServiceResponse;
import com.example.app.model.Product;
import com.example.app.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductServiceResponse> postProducts(@RequestBody Product product) {

        ProductServiceResponse response = productService.createProduct(product);

        return ResponseEntity
            .status(response.getStatus())
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productService.getProducts();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable String id) {

        ProductServiceResponse response = productService.getProduct(id);

        return ResponseEntity
            .status(response.getStatus())
            .body(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> putProduct(@PathVariable String id, @RequestBody Product product) {
        ProductServiceResponse response = productService.putProduct(id, product);

        return ResponseEntity
            .status(response.getStatus())
            .body(response);
    }
}