package com.practice.containerbooking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * This security configuration is ONLY loaded when the "unsecured" profile is active.
 * Its only purpose is to disable Spring's default security (like CSRF)
 * so you can test your API in Postman without authentication.
 */
@Configuration
@EnableWebFluxSecurity
@Profile("unsecured") // <-- Only runs when SPRING_PROFILES_ACTIVE=unsecured
public class UnsecuredSecurityConfig {

    @Bean
    public SecurityWebFilterChain unsecuredSecurityWebFilterChain(ServerHttpSecurity http) {
        http
            // Disable CSRF protection (which causes the 403 error)
            .csrf(csrf -> csrf.disable())
            
            // Allow all requests (no authentication)
            .authorizeExchange(exchange -> exchange
                .anyExchange().permitAll()
            );
        
        return http.build();
    }
}
