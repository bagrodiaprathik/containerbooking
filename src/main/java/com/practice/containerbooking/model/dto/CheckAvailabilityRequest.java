package com.practice.containerbooking.model.dto;

import com.practice.containerbooking.model.enums.ContainerType;
import com.practice.containerbooking.validation.AllowedContainerSize;
import jakarta.validation.constraints.*;

// DTO for Endpoint 1: Check Availability
public record CheckAvailabilityRequest(
    
    @NotNull(message = "Container size is required")
    @AllowedContainerSize
    Integer containerSize,

    @NotNull
    ContainerType containerType,

    @NotBlank @Size(min = 5, max = 20)
    String origin,

    @NotBlank @Size(min = 5, max = 20)
    String destination,

    @NotNull @Min(1) @Max(100)
    Integer quantity
) {}
