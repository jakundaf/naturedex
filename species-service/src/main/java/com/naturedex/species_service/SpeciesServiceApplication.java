package com.naturedex.species_service;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpeciesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpeciesServiceApplication.class, args);
	}

	@PostConstruct
	public void appInit() {
		System.out.println(">>> SpeciesServiceApplication started");
	}


}
