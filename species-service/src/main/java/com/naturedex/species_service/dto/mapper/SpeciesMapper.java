package com.naturedex.species_service.dto.mapper;

import com.naturedex.species_service.dto.SpeciesResponse;
import com.naturedex.species_service.entity.Species;

import java.util.ArrayList;
import java.util.List;

public class SpeciesMapper {

    public static SpeciesResponse mapEntityToSpeciesResponse(Species species) {

        return SpeciesResponse.builder()
                .id(species.getId())
                .name(species.getName())
                .latinName(species.getLatinName())
                .imageUrl(species.getImageUrl())
                .category(species.getCategory())
                .description(species.getDescription())
                .habitat(species.getHabitat())
                .isVisible(species.isVisible())
                .build();

    }

    public static List<SpeciesResponse> mapListOfEntitesToListOfSpeciesResponse(List<Species> list) {

        List<SpeciesResponse> dtoList = new ArrayList<>();

        for (Species s : list) {
            dtoList.add(mapEntityToSpeciesResponse(s));
        }

        return dtoList;

    }

}
