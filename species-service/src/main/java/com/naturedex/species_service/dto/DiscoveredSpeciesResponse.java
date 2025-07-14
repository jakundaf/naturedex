package com.naturedex.species_service.dto;


import com.naturedex.species_service.entity.Species;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class DiscoveredSpeciesResponse {

    private Long id;
    private String username;
    private LocalDateTime discoveredAt;
    private SpeciesResponse speciesResponse;

}

