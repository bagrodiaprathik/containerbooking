package com.practice.containerbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;

// --- THIS IS THE FIX ---
// This line tells Spring Boot to NOT auto-configure security.
// The "excludeName" property was invalid syntax and has been removed.
@SpringBootApplication(exclude = {
    ReactiveSecurityAutoConfiguration.class
})
public class ContainerbookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContainerbookingApplication.class, args);
	}

}

