package com.example.app.config;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginRateLimitFilterTest {

    @Test
    void limitsLoginAttemptsPerRemoteAddress() throws Exception {
        LoginRateLimitFilter filter = new LoginRateLimitFilter();
        AtomicInteger passedRequests = new AtomicInteger();
        FilterChain chain = (request, response) -> passedRequests.incrementAndGet();

        for (int attempt = 0; attempt < 10; attempt++) {
            runLoginRequest(filter, chain);
        }

        MockHttpServletResponse limitedResponse =
            runLoginRequest(filter, chain);

        assertEquals(10, passedRequests.get());
        assertEquals(429, limitedResponse.getStatus());
        assertEquals("60", limitedResponse.getHeader("Retry-After"));
    }

    private MockHttpServletResponse runLoginRequest(
        LoginRateLimitFilter filter,
        FilterChain chain
    ) throws IOException, jakarta.servlet.ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/auth/login");
        request.setServletPath("/auth/login");
        request.setRemoteAddr("192.0.2.10");
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, chain);
        return response;
    }
}
