package com.practice.containerbooking.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.practice.containerbooking.model.enums.ContainerType;

@Document(collection = "bookings")
public class BookingDocument {

    @Id
    private String bookingRef; // This will be our "957xxxxxx" PK [cite: 72]

    @Field("container_size")
    private Integer containerSize;

    @Field("container_type")
    private ContainerType containerType;

    private String origin;
    private String destination;
    private Integer quantity;
    private String timestamp;

    // Constructors, Getters, Setters...
    // ...
}
