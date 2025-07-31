package com.naturedex.user_service.service;

import com.naturedex.user_service.entity.DiscoveredSpecies;
import com.naturedex.user_service.repository.DiscoveredSpeciesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiscoveredSpeciesService {

    private final DiscoveredSpeciesRepository discoveredSpeciesRepository;

    public List<DiscoveredSpecies> getDiscoveredSpecies(@AuthenticationPrincipal Jwt jwt){
        UUID userId = jwt.getClaim("id");

        return discoveredSpeciesRepository.findAllByUserId(userId);
    }

    public boolean hasDiscovered(UUID userId, Long speciesId){
        return discoveredSpeciesRepository.existsByUserIdAndSpeciesId(userId, speciesId);
    }

    public DiscoveredSpecies discover(@AuthenticationPrincipal Jwt jwt, Long speciesId){
        UUID userId = jwt.getClaim("id");

        if (discoveredSpeciesRepository.existsByUserIdAndSpeciesId(userId, speciesId)){
            throw new IllegalStateException("Species already discovered by this user.");
        }

        DiscoveredSpecies discovered = DiscoveredSpecies.builder()
                .userId(userId)
                .speciesId(speciesId)
                .discoveredAt(LocalDateTime.now())
                .build();

        return discoveredSpeciesRepository.save(discovered);
    }
}
