package com.example.app.service;

import com.example.app.model.Product;
import com.example.app.repository.ProductRepository;
import com.example.app.repository.UserRepository;
import com.example.app.model.User;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.app.dto.ProductServiceResponse;
import org.springframework.security.core.Authentication;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public ProductServiceResponse createProduct(Product product) {

        // Product is missing
        if (product == null) {
                return new ProductServiceResponse(
                        400,
                        "Product data is required"
                );
        }

        // Name
        if (product.getName() == null ||
                product.getName().trim().isEmpty()) {

                return new ProductServiceResponse(
                        400,
                        "Product name is required"
                );
        }

        String name = product.getName().trim();

        if (name.length() < 2) {
                return new ProductServiceResponse(
                        400,
                        "Product name must contain at least 2 characters"
                );
        }

        if (name.length() > 100) {
                return new ProductServiceResponse(
                        400,
                        "Product name cannot contain more than 100 characters"
                );
        }

        // Description
        if (product.getDescription() == null ||
                product.getDescription().trim().isEmpty()) {

                return new ProductServiceResponse(
                        400,
                        "Product description is required"
                );
        }

        String description = product.getDescription().trim();

        if (description.length() > 1000) {
                return new ProductServiceResponse(
                        400,
                        "Product description cannot contain more than 1000 characters"
                );
        }

        // Price
        if (product.getPrice() == null) {
                return new ProductServiceResponse(
                        400,
                        "Product price is required"
                );
        }

        if (product.getPrice() <= 0) {
                return new ProductServiceResponse(
                        400,
                        "Product price must be greater than 0"
                );
        }

        // User ID
        if (product.getUserId() == null ||
                product.getUserId().trim().isEmpty()) {

                return new ProductServiceResponse(
                        400,
                        "User ID is required"
                );
        }

        try {

                Product newProduct = new Product(
                        name,
                        description,
                        product.getPrice(),
                        product.getUserId().trim()
                );

                Product savedProduct =
                        productRepository.save(newProduct);

                return new ProductServiceResponse(
                        201,
                        "Product created successfully",
                        savedProduct
                );

        } catch (Exception e) {

                return new ProductServiceResponse(
                        500,
                        "Failed to create product"
                );
        }
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public ProductServiceResponse getProduct(String id) {

        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
                return new ProductServiceResponse(
                404,
                "Product not found",
                null
                );
        }

        return new ProductServiceResponse(
                200,
                "Product found",
                product.get()
        );
   }

   public ProductServiceResponse putProduct(String id, Product request) {

        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isEmpty()) {
                return new ProductServiceResponse(
                        404,
                        "Product not found",
                        null
                );
        }

        Product product = existingProduct.get();

        String ownerId = product.getUserId();

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String loggedUserId = authentication.getName();


        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );

        boolean isOwner = ownerId.equals(loggedUserId);

        System.out.println("this is if its an admin :");
        System.out.println(isAdmin);


        if (isAdmin || isOwner) {

                product.setName(request.getName());
                product.setDescription(request.getDescription());
                product.setPrice(request.getPrice());

                return new ProductServiceResponse(
                        200,
                        "Product updated successfully",
                        productRepository.save(product)
                );
        }

        return new ProductServiceResponse(
                403,
                "User has no access to this product",
                null
        );
   }
}