package com.practice.containerbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// We no longer need the exclude here.
// import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;

// --- THIS IS THE FIX ---
// We remove the 'exclude' attribute.
// This will allow Spring to load your controllers again.
@SpringBootApplication
public class ContainerbookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContainerbookingApplication.class, args);
	}

}


