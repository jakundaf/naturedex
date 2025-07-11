package com.naturedex.observation_service.controller;

import com.naturedex.observation_service.dto.ObservationRequest;
import com.naturedex.observation_service.dto.ObservationResponse;
import com.naturedex.observation_service.service.ObservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/observations")
@RequiredArgsConstructor
public class ObservationController {

    private final ObservationService observationService;

    @PostMapping
    public ResponseEntity<ObservationResponse> createObservation(@RequestBody ObservationRequest request, @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(observationService.createObservation(request, jwt), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ObservationResponse>> getUserObservations(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(observationService.getUserObservations(jwt), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObservationResponse> getObservationById(Long id, @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(observationService.getObservationById(id, jwt), HttpStatus.OK);
    }

}
