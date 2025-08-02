package com.naturedex.species_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final WebClient userServiceWebClient;

    public List<Long> getDiscoveredSpecies(String token){
        return userServiceWebClient.get()
                .uri("/api/users/me/discovered")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToFlux(DiscoveredSpeciesDto.class)
                .map(DiscoveredSpeciesDto::speciesId)
                .collectList()
                .block();
    }

    public record DiscoveredSpeciesDto(Long speciesId, LocalDateTime discoveredAt) {}

}
