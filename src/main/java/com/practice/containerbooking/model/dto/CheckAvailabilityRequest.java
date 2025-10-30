package com.practice.containerbooking.model.dto;

import jakarta.validation.constraints.*;
import com.practice.containerbooking.model.enums.ContainerType;

// DTO for Endpoint 1: Check Availability 
public record CheckAvailabilityRequest(
    @NotNull
    Integer containerSize, // Should add custom validation for (20, 40)

    @NotNull
    ContainerType containerType,

    @NotBlank @Size(min = 5, max = 20)
    String origin,

    @NotBlank @Size(min = 5, max = 20)
    String destination,

    @NotNull @Min(1) @Max(100)
    Integer quantity
) {}
