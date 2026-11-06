package com.example.app.service;

import com.example.app.dto.ProductServiceResponse;
import com.example.app.model.Product;
import com.example.app.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createProductUsesAuthenticatedUserInsteadOfSubmittedOwner() {
        authenticateAs("user-123");
        when(productRepository.save(any(Product.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        ProductServiceResponse response = productService.createProduct(
            new Product("Keyboard", "Mechanical keyboard", 89.99, "other-user")
        );

        assertEquals(201, response.getStatus());
        assertEquals("user-123", response.getProduct().getUserId());
    }

    @Test
    void updateRejectsNonOwner() {
        authenticateAs("user-123");
        Product product = new Product("Keyboard", "Mechanical keyboard", 89.99, "owner-456");
        when(productRepository.findById("product-1")).thenReturn(Optional.of(product));

        ProductServiceResponse response = productService.putProduct(
            "product-1",
            new Product("Changed", "Changed description", 50.0, "user-123")
        );

        assertEquals(403, response.getStatus());
        assertEquals("Keyboard", product.getName());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void ownerCannotChangeProductOwnershipDuringUpdate() {
        authenticateAs("owner-456");
        Product product = new Product("Keyboard", "Mechanical keyboard", 89.99, "owner-456");
        when(productRepository.findById("product-1")).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductServiceResponse response = productService.putProduct(
            "product-1",
            new Product("Changed", "Changed description", 50.0, "attacker")
        );

        assertEquals(200, response.getStatus());
        assertEquals("owner-456", response.getProduct().getUserId());
    }

    private void authenticateAs(String userId) {
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(
                userId,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
            )
        );
    }
}
