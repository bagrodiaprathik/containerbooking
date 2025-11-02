package com.practice.containerbooking.service;

import com.practice.containerbooking.model.entity.DatabaseSequence;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {

    private final ReactiveMongoOperations mongoOperations;

    public Mono<String> getNextBookingId() {
        // Define the query to find our specific counter
        Query query = new Query(Criteria.where("_id").is("booking_sequence"));
        
        // Define the update to increment the sequence by 1
        Update update = new Update().inc("seq", 1);
        
        // Options: return the *new* value after update, and create if it doesn't exist
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);


        return mongoOperations.findAndModify(query, update, options, DatabaseSequence.class)
            .map(counter -> "957" + String.format("%06d", counter.getSeq()));
    }
}
