package com.naturedex.observation_service.exception;

public class ObservationNotFoundException extends RuntimeException {

    public ObservationNotFoundException(String message) {
        super(message);
    }
}
