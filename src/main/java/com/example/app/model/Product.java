package com.example.app.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @Field("name")
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Field("description")
    @NotBlank
    @Size(max = 1000)
    private String description;

    @Field("price")
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private Double price;

    @Field("userId")
    private String userId;

    public Product() {
    }

    public Product(String name, String description, Double price, String userId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}