package com.naturedex.user_service.controller;

import com.naturedex.user_service.entity.DiscoveredSpecies;
import com.naturedex.user_service.service.DiscoveredSpeciesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users/me/discovered")
@RequiredArgsConstructor
public class DiscoveredSpeciesController {

    private final DiscoveredSpeciesService discoveredSpeciesService;

    @GetMapping
    public ResponseEntity<List<DiscoveredSpecies>> getMyDiscoveredSpecies(@AuthenticationPrincipal Jwt jwt){
        return new ResponseEntity<>(discoveredSpeciesService.getDiscoveredSpecies(jwt), HttpStatus.OK);
    }

    // TODO
    // @GetMapping("/{speciesId}")
    // public ResponseEntity<DiscoveredSpecies> getMySingleDiscoveredSpecies(@AuthenticationPrincipal Jwt jwt
    // @PathVariable Long speciesId){}

    @PostMapping("/{speciesId}")
    public ResponseEntity<String> discoverSpecies(@PathVariable Long speciesId, @AuthenticationPrincipal Jwt jwt){
        return new ResponseEntity<>(discoveredSpeciesService.discover(jwt, speciesId), HttpStatus.OK);
    }


}
