package com.example.app.dto;

import com.example.app.model.Product;

public class ProductServiceResponse {

    private int status;
    private String message;
    private Product product;

    public ProductServiceResponse(
            int status,
            String message
    ) {
        this.status = status;
        this.message = message;
    }

    public ProductServiceResponse(
            int status,
            String message,
            Product product
    ) {
        this.status = status;
        this.message = message;
        this.product = product;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Product getProduct() {
        return product;
    }
}