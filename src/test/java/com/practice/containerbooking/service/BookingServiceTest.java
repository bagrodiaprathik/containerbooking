package com.practice.containerbooking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.containerbooking.model.dto.CheckAvailabilityRequest;
import com.practice.containerbooking.model.dto.CheckAvailabilityResponse;
import com.practice.containerbooking.model.dto.CreateBookingRequest;
import com.practice.containerbooking.model.dto.CreateBookingResponse;
import com.practice.containerbooking.model.dto.ExternalAvailabilityResponse;
import com.practice.containerbooking.model.entity.BookingDocument;
import com.practice.containerbooking.model.enums.ContainerType;
import com.practice.containerbooking.repository.BookingRepository;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) 
public class BookingServiceTest {

    public static MockWebServer mockBackEnd;
    private BookingService bookingService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private SequenceGeneratorService sequenceGenerator;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        // Set up the WebClient to point to our mock server
        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        // Create a new service instance for each test
        bookingService = new BookingService(webClient, bookingRepository, sequenceGenerator);
    }

    @Test
    void checkAvailabilityShouldReturnTrueWhenSpaceIsAvailable() throws Exception {
        // Arrange: Prepare a mock response from the external API
        ExternalAvailabilityResponse mockApiResponse = new ExternalAvailabilityResponse(6);
        mockBackEnd.enqueue(new MockResponse()
            .setBody(objectMapper.writeValueAsString(mockApiResponse))
            .addHeader("Content-Type", "application/json"));

        CheckAvailabilityRequest request = new CheckAvailabilityRequest(20, ContainerType.DRY, "Southampton", "Singapore", 5);

        // Act: Call the service method
        Mono<CheckAvailabilityResponse> responseMono = bookingService.checkAvailability(request);

        // Assert: Verify the reactive stream
        StepVerifier.create(responseMono)
            .expectNextMatches(response -> response.available() == true) // The available space 6 > 0
            .verifyComplete();
    }

    @Test
    void checkAvailabilityShouldReturnFalseWhenSpaceIsZero() throws Exception {
        // Arrange: Prepare a mock response with 0 space
        ExternalAvailabilityResponse mockApiResponse = new ExternalAvailabilityResponse(0);
        mockBackEnd.enqueue(new MockResponse()
            .setBody(objectMapper.writeValueAsString(mockApiResponse))
            .addHeader("Content-Type", "application/json"));

        CheckAvailabilityRequest request = new CheckAvailabilityRequest(20, ContainerType.DRY, "Southampton", "Singapore", 5);

        // Act
        Mono<CheckAvailabilityResponse> responseMono = bookingService.checkAvailability(request);

        // Assert
        StepVerifier.create(responseMono)
            .expectNextMatches(response -> response.available() == false) // The available space 0 is not > 0
            .verifyComplete();
    }

    @Test
    void createBookingShouldReturnBookingRef() {
        // Arrange
        CreateBookingRequest request = new CreateBookingRequest(20, ContainerType.DRY, "Southampton", "Singapore", 5, "2024-10-12T13:53:09Z");
        
        // Mock the document that will be "saved"
        BookingDocument savedDocument = new BookingDocument();
        savedDocument.setBookingRef("957000001");

        // Mock the dependencies' behavior
        when(sequenceGenerator.getNextBookingId()).thenReturn(Mono.just("957000001"));
        when(bookingRepository.save(any(BookingDocument.class))).thenReturn(Mono.just(savedDocument));

        // Act
        Mono<CreateBookingResponse> responseMono = bookingService.createBooking(request);

        // Assert
        StepVerifier.create(responseMono)
            .expectNextMatches(response -> response.bookingRef().equals("957000001"))
            .verifyComplete();
    }
}

