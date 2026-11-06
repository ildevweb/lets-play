package com.example.app.controller;

import com.example.app.dto.ProductServiceResponse;
import com.example.app.model.Product;
import com.example.app.service.ProductService;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProductServiceResponse> postProducts(
        @Valid @RequestBody Product product
    ) {

        ProductServiceResponse response = productService.createProduct(product);

        return ResponseEntity
            .status(response.getStatus())
            .body(response);
    }

    @GetMapping
    @PermitAll
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productService.getProducts();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<?> getProduct(@PathVariable String id) {

        ProductServiceResponse response = productService.getProduct(id);

        return ResponseEntity
            .status(response.getStatus())
            .body(response);
    }


    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> putProduct(
        @PathVariable String id,
        @Valid @RequestBody Product product
    ) {
        ProductServiceResponse response = productService.putProduct(id, product);

        return ResponseEntity
            .status(response.getStatus())
            .body(response);
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        ProductServiceResponse response = productService.deleteProduct(id);

        return ResponseEntity
            .status(response.getStatus())
            .body(response);
    }
}