package com.practice.containerbooking;

import org.springframework.boot.SpringApplication;

public class TestContainerbookingApplication {

	public static void main(String[] args) {
		SpringApplication.from(ContainerbookingApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
