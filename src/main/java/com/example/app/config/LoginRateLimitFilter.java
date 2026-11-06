package com.example.app.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private static final long WINDOW_MILLIS = 60_000;
    private static final int MAX_REQUESTS = 10;

    private final ConcurrentHashMap<String, Window> attempts =
        new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        if (!"POST".equals(request.getMethod())
            || !"/auth/login".equals(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        long now = System.currentTimeMillis();
        AtomicBoolean limited = new AtomicBoolean(false);
        Window window = attempts.compute(request.getRemoteAddr(), (address, current) -> {
            if (current == null || now - current.startedAt() >= WINDOW_MILLIS) {
                return new Window(now, 1);
            }
            if (current.requestCount() >= MAX_REQUESTS) {
                limited.set(true);
                return current;
            }
            return new Window(current.startedAt(), current.requestCount() + 1);
        });

        if (limited.get()) {
            long retryAfterSeconds =
                Math.max(1, (window.startedAt() + WINDOW_MILLIS - now + 999) / 1000);
            response.setStatus(429);
            response.setHeader("Retry-After", Long.toString(retryAfterSeconds));
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"status\":429,\"message\":\"Too many login attempts; try again later\"}"
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Scheduled(fixedDelay = WINDOW_MILLIS)
    public void removeExpiredWindows() {
        long cutoff = System.currentTimeMillis() - WINDOW_MILLIS;
        attempts.entrySet().removeIf(entry -> entry.getValue().startedAt() < cutoff);
    }

    private record Window(long startedAt, int requestCount) {
    }
}
