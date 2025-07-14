package com.naturedex.species_service.exception;

public class SpeciesNotFoundException extends RuntimeException {

    public SpeciesNotFoundException(String message) {
        super(message);
    }
}
