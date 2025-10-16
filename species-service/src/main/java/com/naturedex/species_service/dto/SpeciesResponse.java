package com.naturedex.species_service.dto;

import com.naturedex.species_service.entity.Species;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SpeciesResponse {

    private Long id;
    private String name;
    private String latinName;
    private String imageUrl;
    private Species.Category category;
    private String description;
    private String habitat;
    private boolean isVisible;

}
