package com.practice.containerbooking.model.dto;

import com.practice.containerbooking.model.enums.ContainerType;
import com.practice.containerbooking.validation.AllowedContainerSize;
import jakarta.validation.constraints.*;

// DTO for Endpoint 2: Create Booking
public record CreateBookingRequest(

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
    Integer quantity,

    // Basic ISO-8601 UTC validation
    @NotBlank @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$", message = "Timestamp must be in ISO-8601 UTC format (e.g., 2020-10-12T13:53:09Z)")
    String timestamp
) {}
