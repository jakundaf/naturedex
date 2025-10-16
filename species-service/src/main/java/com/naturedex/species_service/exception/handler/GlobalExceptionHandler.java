package com.naturedex.species_service.exception.handler;

import com.naturedex.species_service.exception.SpeciesNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SpeciesNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSpeciesNotFound(SpeciesNotFoundException ex, HttpServletRequest request){

        return buildResponseEntity(HttpStatus.NOT_FOUND, ex, request);

    }

    private static ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus status, Exception ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }

}
