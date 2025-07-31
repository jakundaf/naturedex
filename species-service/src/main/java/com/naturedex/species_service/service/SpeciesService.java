package com.naturedex.species_service.service;


import com.naturedex.species_service.dto.DiscoveredSpeciesResponse;
import com.naturedex.species_service.dto.SpeciesResponse;
import com.naturedex.species_service.dto.mapper.SpeciesMapper;
import com.naturedex.species_service.entity.DiscoveredSpecies;
import com.naturedex.species_service.entity.Species;
import com.naturedex.species_service.exception.SpeciesNotFoundException;
import com.naturedex.species_service.repository.DiscoveredSpeciesRepository;
import com.naturedex.species_service.repository.SpeciesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.naturedex.species_service.dto.mapper.SpeciesMapper.mapListOfEntitesToListOfSpeciesResponse;

@Service
@RequiredArgsConstructor
public class SpeciesService {


    private final SpeciesRepository speciesRepository;
    private final DiscoveredSpeciesRepository discoveredSpeciesRepository;

    public List<SpeciesResponse> getAllSpecies() {
        return mapListOfEntitesToListOfSpeciesResponse(speciesRepository.findAll());
    }

    public List<DiscoveredSpecies> getDiscoveredSpecies(Jwt jwt) {
        String username = jwt.getClaim("username");

        return discoveredSpeciesRepository.findByUsername(username);
    }

    public DiscoveredSpeciesResponse markAsDiscovered(Long speciesId, Jwt jwt) {

        String username = jwt.getClaim("username");
        Species species = speciesRepository.findById(speciesId)
                .orElseThrow(() -> new SpeciesNotFoundException("No species with id: " + speciesId + " found."));

        boolean alreadyDiscovered = discoveredSpeciesRepository.existsByUsernameAndSpecies(username, species);
        if (!alreadyDiscovered) {
            discoveredSpeciesRepository.save(DiscoveredSpecies.builder()
                    .username(username)
                    .discoveredAt(LocalDateTime.now())
                    .species(species)
                    .build());

            SpeciesResponse speciesResponse = SpeciesMapper.mapEntityToSpeciesResponse(species);

            return DiscoveredSpeciesResponse.builder()
                    .id(speciesId)
                    .speciesResponse(speciesResponse)
                    .discoveredAt(LocalDateTime.now())
                    .username(username)
                    .build();
        }

        SpeciesResponse speciesResponse = SpeciesMapper.mapEntityToSpeciesResponse(species);

        return DiscoveredSpeciesResponse.builder()
                .id(speciesId)
                .speciesResponse(speciesResponse)
                .discoveredAt(LocalDateTime.now())
                .username(username)
                .build();

    }
}