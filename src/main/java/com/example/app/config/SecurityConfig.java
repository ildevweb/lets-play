package com.example.app.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LoginRateLimitFilter loginRateLimitFilter;
    private final String allowedOrigins;

    public SecurityConfig(
        JwtAuthenticationFilter jwtAuthenticationFilter,
        LoginRateLimitFilter loginRateLimitFilter,
        @Value("${app.cors.allowed-origins:}") String allowedOrigins
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.loginRateLimitFilter = loginRateLimitFilter;
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
            .map(String::trim)
            .filter(origin -> !origin.isEmpty())
            .toList();

        if (origins.contains("*")) {
            throw new IllegalStateException(
                "CORS_ALLOWED_ORIGINS must list explicit origins; '*' is not allowed"
            );
        }

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(
            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );
        configuration.setAllowedHeaders(
            List.of("Authorization", "Content-Type")
        );
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public FilterRegistrationBean<LoginRateLimitFilter> loginRateLimitFilterRegistration(
        LoginRateLimitFilter filter
    ) {
        FilterRegistrationBean<LoginRateLimitFilter> registration =
            new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http
    ) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // Public authentication endpoints
                .requestMatchers(
                    "/auth/login",
                    "/auth/register"
                ).permitAll()

                // Products
                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/products/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/products/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/products/**").authenticated()

                //Users
                .requestMatchers("/users/**").hasRole("ADMIN")


                // Everything else requires JWT
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            )
            .addFilterBefore(loginRateLimitFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}