package com.practice.containerbooking.service;

import com.practice.containerbooking.model.entity.DatabaseSequence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

/**
 * This is an INTEGRATION TEST for the SequenceGeneratorService.
 * It uses Testcontainers to start a real MongoDB instance.
 * It uses @SpringBootTest to load the full application context (needed for ReactiveMongoOperations).
 */
@SpringBootTest
@Testcontainers
class SequenceGeneratorServiceTest {

    // This annotation tells JUnit to manage the container
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    // This method overrides the application's MongoDB URI with the temporary container's address
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private SequenceGeneratorService sequenceGenerator;

    @Test
    void shouldIncrementSequenceAndReturnCorrectId() {
        // We test the service twice to ensure it increments.

        // 1. First call
        StepVerifier.create(sequenceGenerator.getNextBookingId())
            .expectNext("957000001") // Expect the first ID
            .verifyComplete();

        // 2. Second call
        StepVerifier.create(sequenceGenerator.getNextBookingId())
            .expectNext("957000002") // Expect the second ID
            .verifyComplete();
    }
}

