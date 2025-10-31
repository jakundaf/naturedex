package com.naturedex.user_service.service;

import com.naturedex.user_service.entity.DiscoveredSpecies;
import com.naturedex.user_service.repository.DiscoveredSpeciesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiscoveredSpeciesService {

    private final DiscoveredSpeciesRepository discoveredSpeciesRepository;

    public List<DiscoveredSpecies> getDiscoveredSpecies(Jwt jwt) {
        String userId = jwt.getClaim("id");

        return discoveredSpeciesRepository.findAllByUserId(userId);
    }

    public String discover(Jwt jwt, Long speciesId) {
        String userId = jwt.getClaim("id");

        if (discoveredSpeciesRepository.existsByUserIdAndSpeciesId(userId, speciesId)) {
            throw new IllegalStateException("Species already discovered by this user.");
        }

        DiscoveredSpecies discovered = DiscoveredSpecies.builder()
                .userId(userId)
                .speciesId(speciesId)
                .discoveredAt(LocalDateTime.now())
                .build();



        discoveredSpeciesRepository.save(discovered);

        return "Species with id '" + speciesId + "' has been discovered for user with id '" + userId + "'.";
    }
}
