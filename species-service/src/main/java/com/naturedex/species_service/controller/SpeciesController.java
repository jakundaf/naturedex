package com.naturedex.species_service.controller;


import com.naturedex.species_service.dto.SpeciesCatalogEntry;
import com.naturedex.species_service.dto.SpeciesResponse;
import com.naturedex.species_service.service.SpeciesService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/species")
@RequiredArgsConstructor
public class SpeciesController {

    private final SpeciesService speciesService;

    @PostConstruct
    public void init() {
        log.warn(">>> SpeciesController loaded");
    }

    @GetMapping
    public ResponseEntity<List<SpeciesResponse>> getAllSpecies() {
        log.info("Species Controller - getAllSpecies endpoint called.");
        return new ResponseEntity<>(speciesService.getAllSpecies(), HttpStatus.OK);
    }

    @GetMapping("/catalog")
    public ResponseEntity<List<SpeciesCatalogEntry>> getSpeciesCatalog(@AuthenticationPrincipal Jwt jwt){
        return new ResponseEntity<>(speciesService.getSpeciesCatalog(jwt), HttpStatus.OK);
    }
}
