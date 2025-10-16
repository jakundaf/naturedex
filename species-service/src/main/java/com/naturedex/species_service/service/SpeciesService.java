package com.naturedex.species_service.service;


import com.naturedex.species_service.client.UserServiceClient;
import com.naturedex.species_service.dto.SpeciesCatalogEntry;
import com.naturedex.species_service.dto.SpeciesResponse;
import com.naturedex.species_service.entity.Species;
import com.naturedex.species_service.repository.SpeciesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.naturedex.species_service.dto.mapper.SpeciesMapper.mapListOfEntitesToListOfSpeciesResponse;

@Service
@RequiredArgsConstructor
public class SpeciesService {


    private final SpeciesRepository speciesRepository;
    private final UserServiceClient userServiceClient;

    public List<SpeciesResponse> getAllSpecies() {
        return mapListOfEntitesToListOfSpeciesResponse(speciesRepository.findAll());
    }

    public List<SpeciesCatalogEntry> getSpeciesCatalog(Jwt jwt){
        String token = jwt.getTokenValue();
        List<Species> allSpecies = speciesRepository.findAll();
        List<Long> discoveredIds = userServiceClient.getDiscoveredSpecies(token);

        return allSpecies.stream()
                .map(species -> new SpeciesCatalogEntry(
                        species.getId(),
                        species.getName(),
                        species.getLatinName(),
                        species.getImageUrl(),
                        species.getCategory().name(),
                        species.getDescription(),
                        species.getHabitat(),
                        discoveredIds.contains(species.getId())
                ))
                .toList();
    }

}