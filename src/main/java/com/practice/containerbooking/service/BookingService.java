package com.practice.containerbooking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.practice.containerbooking.model.dto.CheckAvailabilityRequest;
import com.practice.containerbooking.model.dto.CheckAvailabilityResponse;
import com.practice.containerbooking.model.dto.CreateBookingRequest;
import com.practice.containerbooking.model.dto.CreateBookingResponse;
import com.practice.containerbooking.model.dto.ExternalAvailabilityResponse;
import com.practice.containerbooking.model.entity.BookingDocument;
import com.practice.containerbooking.repository.BookingRepository;

import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor // (from Lombok)
public class BookingService {

    private final WebClient maerskWebClient;
    private final BookingRepository bookingRepository;
    private final SequenceGeneratorService sequenceGenerator;
    

    public Mono<CheckAvailabilityResponse> checkAvailability(CheckAvailabilityRequest request) {
        
        
        return maerskWebClient.get() 
            .retrieve()
            .bodyToMono(ExternalAvailabilityResponse.class)
            .map(response -> new CheckAvailabilityResponse(response.availableSpace() > 0))
            .retry(3)
            .onErrorResume(ex -> {
                // Handle errors (e.g., external API is down)
                // For now, assume unavailable
                return Mono.just(new CheckAvailabilityResponse(false));
            });
    }

    public Mono<CreateBookingResponse> createBooking(CreateBookingRequest request) {
        return sequenceGenerator.getNextBookingId()
            .flatMap(bookingId -> {
                BookingDocument doc = new BookingDocument();
                // Map fields from request to doc
                doc.setBookingRef(bookingId); 
                doc.setContainerSize(request.containerSize());
                doc.setContainerType(request.containerType());
                doc.setOrigin(request.origin());
                doc.setDestination(request.destination());
                doc.setQuantity(request.quantity());
                doc.setTimestamp(request.timestamp());
                return bookingRepository.save(doc);
            })
            .map(savedDoc -> new CreateBookingResponse(savedDoc.getBookingRef()));
    }
}

