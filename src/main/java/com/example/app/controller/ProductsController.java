package com.example.app.controller;

import com.example.app.dto.ProductServiceResponse;
import com.example.app.model.Product;
import com.example.app.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductServiceResponse> postProducts(
            @RequestBody Product product
    ) {

        ProductServiceResponse response =
            productService.createProduct(product);

        return ResponseEntity
            .status(response.getStatus())
            .body(response);
    }
}