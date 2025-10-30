package com.practice.containerbooking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor // (from Lombok)
public class BookingService {

    private final WebClient maerskWebClient;
    

    public Mono<CheckAvailabilityResponse> checkAvailability(CheckAvailabilityRequest request) {
        // You can pass the request body to the external API if needed,
        // though the spec doesn't explicitly say to.
        // For this example, we'll just call the endpoint.
        
        return maerskWebClient.get() // Or .post() if it expects the request body
            .retrieve()
            .bodyToMono(ExternalAvailabilityResponse.class)
            .map(response -> new CheckAvailabilityResponse(response.availableSpace() > 0)) // [cite: 44, 48]
            .retry(3) // Simple retry as required by "reactive programming paradigms" [cite: 21]
            .onErrorResume(ex -> {
                // Handle errors (e.g., external API is down)
                // For now, assume unavailable
                return Mono.just(new CheckAvailabilityResponse(false));
            });
    }
}
