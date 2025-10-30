package com.practice.containerbooking.model.entity;

@Document(collection = "database_sequences")
public class DatabaseSequence {
    @Id
    private String id;
    private long seq;
    // Getters/Setters...
}
