package com.projekt.Spring_Boot_API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootApiApplication.class, args);
	}

	// TODO: DTOs to better manage what response is sent for GETs
	// TODO: return authenticatedUser in get-self again when there is a DTO to manage the response
	// TODO: yellow lines in authenticate help methods in ItemService, FolderService and UserService
	// TODO: better exception logging in ItemService
	// TODO: comments following Javadoc standards in all services

}
