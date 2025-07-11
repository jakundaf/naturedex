package com.naturedex.observation_service.service;


import com.naturedex.observation_service.dto.ObservationRequest;
import com.naturedex.observation_service.dto.ObservationResponse;
import com.naturedex.observation_service.dto.mapper.ObservationMapper;
import com.naturedex.observation_service.entity.Observation;
import com.naturedex.observation_service.exception.ObservationNotFoundException;
import com.naturedex.observation_service.repository.ObservationRepository;
import com.naturedex.observation_service.utils.ObservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.naturedex.observation_service.dto.mapper.ObservationMapper.mapEntityToDtoResponse;

@Service
@RequiredArgsConstructor
public class ObservationService {

    private final ObservationRepository observationRepository;
    private final UserClientService userClientService;

    public ObservationResponse createObservation(ObservationRequest request, Jwt jwt) {

        String username = jwt.getClaim("username");

        Observation observation = Observation.builder()
                .imageUrl(request.getImageUrl())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .observedAt(request.getObservedAt())
                .status(ObservationStatus.UNRECOGNIZED)
                .speciesName(null)
                .username(username)
                .build();

        observationRepository.save(observation);

        return mapEntityToDtoResponse(observation);

    }

    public List<ObservationResponse> getUserObservations(Jwt jwt) {

        String username = jwt.getClaim("username");

        return observationRepository.findByUsername(username)
                .stream()
                .map(ObservationMapper::mapEntityToDtoResponse)
                .toList();
    }

    public ObservationResponse getObservationById(Long id, Jwt jwt) {

        String username = jwt.getClaim("username");

        return mapEntityToDtoResponse(observationRepository.findByIdAndUsername(id, username)
                .orElseThrow(() -> new ObservationNotFoundException("No observation with id: "
                        + id + " found for username: " + username)));

    }


}
