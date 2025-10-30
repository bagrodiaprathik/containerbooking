package com.practice.containerbooking.service;

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
            .map(counter -> "957" + String.format("%06d", counter.getSeq())); // Formats 1 as "957000001"
    }
}
