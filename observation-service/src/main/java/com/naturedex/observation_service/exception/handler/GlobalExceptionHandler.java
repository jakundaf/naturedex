package com.naturedex.observation_service.exception.handler;

import com.naturedex.observation_service.exception.InvalidTokenException;
import com.naturedex.observation_service.exception.ObservationNotFoundException;
import com.naturedex.observation_service.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObservationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleObservationNotFound(ObservationNotFoundException ex, HttpServletRequest request) {

        return buildResponseEntity(HttpStatus.NOT_FOUND, ex, request);

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(InvalidTokenException ex, HttpServletRequest request) {

        return buildResponseEntity(HttpStatus.UNAUTHORIZED, ex, request);

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
