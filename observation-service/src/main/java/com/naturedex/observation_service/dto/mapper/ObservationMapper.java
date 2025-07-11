package com.naturedex.observation_service.dto.mapper;


import com.naturedex.observation_service.dto.ObservationResponse;
import com.naturedex.observation_service.entity.Observation;

public class ObservationMapper {

    public static ObservationResponse mapEntityToDtoResponse(Observation observation) {

        return ObservationResponse.builder()
                .imageUrl(observation.getImageUrl())
                .latitude(observation.getLatitude())
                .longitude(observation.getLongitude())
                .observedAt(observation.getObservedAt())
                .status(observation.getStatus())
                .speciesName(observation.getSpeciesName())
                .build();

    }

}
