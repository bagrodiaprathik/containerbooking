package com.practice.containerbooking.repository;

import com.practice.containerbooking.model.entity.BookingDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface BookingRepository extends ReactiveMongoRepository<BookingDocument, String> {
}