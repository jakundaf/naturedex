package com.naturedex.species_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SpeciesCatalogEntry {

    private Long id;
    private String name;
    private String latinName;
    private String imageUrl;
    private String category;
    private String description;
    private String habitat;
    private boolean isDiscovered;
}
