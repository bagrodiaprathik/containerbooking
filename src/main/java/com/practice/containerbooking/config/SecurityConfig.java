package com.practice.containerbooking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile; // <-- Import this
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Configures the application's security settings using Spring Security
 * for a reactive (WebFlux) application.
 */
@Configuration
@EnableWebFluxSecurity
@Profile("!unsecured") // <-- THIS IS THE SWITCH: Only apply security if 'unsecured' profile is NOT active
public class SecurityConfig {

    /**
     * This is the central piece of reactive security.
     * It defines a "security filter chain" that all requests must pass through.
     * @param http The ServerHttpSecurity to configure.
     * @return A configured SecurityWebFilterChain.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            // 1. Disable CSRF (Cross-Site Request Forgery) - not needed for stateless REST APIs
            .csrf(csrf -> csrf.disable())
            
            // 2. Configure authorization rules
            .authorizeExchange(exchange -> exchange
                // 3. Allow requests to the OpenAPI spec (if you add it)
                .pathMatchers("/openapi.yml").permitAll()
                // 4. All other requests to /api/bookings/** must be authenticated
                .pathMatchers("/api/bookings/**").authenticated()
                // 5. Any other request (e.g., to actuator/health) can be permitted (adjust as needed)
                .anyExchange().permitAll()
            )
            
            // 6. Configure this app as an OAuth 2.0 Resource Server
            .oauth2ResourceServer(oauth2 -> oauth2
                // 7. Tell it to validate JWTs (JSON Web Tokens)
                .jwt(jwt -> {})
            );

        // 8. Build and return the filter chain
        return http.build();
    }
}

