package com.projekt.Spring_Boot_API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootApiApplication.class, args);
	}

	// TODO: UserService needs authentication (who can use GETs on users? Should they exist at all? Get own data instead of getById?)
	// TODO: look into simply passing request straight into services to minimize arguments
    // TODO: comments following Javadoc standards in all services
	// TODO: yellow lines in authenticate help methods in ItemService and FolderService
	// TODO: better exception logging in uploadItem in ItemService
	// TODO: DTOs to better manage what response is sent for GETs
	// TODO: Search function for files?

}
