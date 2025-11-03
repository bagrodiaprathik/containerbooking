package com.practice.containerbooking.controller;

import com.practice.containerbooking.model.dto.CheckAvailabilityRequest;
import com.practice.containerbooking.model.dto.CheckAvailabilityResponse;
import com.practice.containerbooking.model.dto.CreateBookingRequest;
import com.practice.containerbooking.model.dto.CreateBookingResponse;
import com.practice.containerbooking.model.enums.ContainerType;
import com.practice.containerbooking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(
    controllers = BookingController.class,
    excludeAutoConfiguration = {
        MongoReactiveAutoConfiguration.class,
        MongoReactiveDataAutoConfiguration.class
    }
)
@ActiveProfiles("unsecured")
public class BookingControllerTest {

    @Autowired
    private WebTestClient webTestClient; // A client for testing WebFlux endpoints

    @MockBean
    private BookingService bookingService; // A mock version of the service

    @Test
    void shouldReturnAvailableTrueWhenServiceSucceeds() {
        // Arrange: Create a valid request
        CheckAvailabilityRequest request = new CheckAvailabilityRequest(20, ContainerType.DRY, "Southampton", "Singapore", 5);

        // Mock the service's behavior
        when(bookingService.checkAvailability(any(CheckAvailabilityRequest.class)))
            .thenReturn(Mono.just(new CheckAvailabilityResponse(true)));

        // Act & Assert
        webTestClient.post().uri("/api/bookings/check-availability")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.available").isEqualTo(true);
    }

    @Test
    void shouldReturnBookingRefWhenBookingIsCreated() {
        // Arrange: Create a valid request
        CreateBookingRequest request = new CreateBookingRequest(20, ContainerType.DRY, "Southampton", "Singapore", 5, "2024-10-12T13:53:09Z");

        // Mock the service's behavior
        when(bookingService.createBooking(any(CreateBookingRequest.class)))
            .thenReturn(Mono.just(new CreateBookingResponse("957000001")));

        // Act & Assert
        webTestClient.post().uri("/api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk() // 200 OK (or .isCreated() for 201)
            .expectBody()
            .jsonPath("$.bookingRef").isEqualTo("957000001");
    }

    @Test
    void shouldReturnBadRequestForInvalidContainerSize() {
        // Arrange: Create a request with an invalid size (e.g., 30)
        CreateBookingRequest invalidRequest = new CreateBookingRequest(30, ContainerType.DRY, "Southampton", "Singapore", 5, "2024-10-12T13:53:09Z");

        // Act & Assert
        webTestClient.post().uri("/api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(invalidRequest)
            .exchange()
            .expectStatus().isBadRequest() // Expect a 400 Bad Request
            .expectBody()
            .jsonPath("$.message").isEqualTo("Container size must be either 20 or 40");
    }
}

