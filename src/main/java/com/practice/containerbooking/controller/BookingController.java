package com.practice.containerbooking.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import com.practice.containerbooking.service.BookingService;
import com.practice.containerbooking.model.dto.CheckAvailabilityRequest;
import com.practice.containerbooking.model.dto.CheckAvailabilityResponse;
import com.practice.containerbooking.model.dto.CreateBookingRequest;
import com.practice.containerbooking.model.dto.CreateBookingResponse;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/check-availability")
    public Mono<CheckAvailabilityResponse> checkAvailability(
        @Valid @RequestBody Mono<CheckAvailabilityRequest> request
    ) {
        
        return request.flatMap(bookingService::checkAvailability);
    }

    @PostMapping
    public Mono<CreateBookingResponse> createBooking(
        @Valid @RequestBody Mono<CreateBookingRequest> request
    ) {
        return request.flatMap(bookingService::createBooking);
    }
}

