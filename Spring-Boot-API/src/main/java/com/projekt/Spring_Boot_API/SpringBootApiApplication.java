package com.projekt.Spring_Boot_API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootApiApplication.class, args);
	}

	// TODO: move service helper methods to a utils folder to minimize repetition
	// TODO: full API test of all methods and mappings
	// TODO: consider removing getAllUsers method post testing
	// TODO: clean code check, uniformity check, no dead imports check etc.

}
