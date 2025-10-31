package com.naturedex.observation_service.service;


import com.naturedex.observation_service.client.UserServiceClient;
import com.naturedex.observation_service.dto.ObservationRequest;
import com.naturedex.observation_service.dto.ObservationResponse;
import com.naturedex.observation_service.dto.UpdateObservationStatusRequest;
import com.naturedex.observation_service.dto.mapper.ObservationMapper;
import com.naturedex.observation_service.entity.Observation;
import com.naturedex.observation_service.exception.ObservationNotFoundException;
import com.naturedex.observation_service.repository.ObservationRepository;
import com.naturedex.observation_service.utils.ObservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.naturedex.observation_service.dto.mapper.ObservationMapper.mapEntityToDtoResponse;

@Service
@RequiredArgsConstructor
public class ObservationService {

    private final ObservationRepository observationRepository;
    private final UserServiceClient userClientService;

    public ObservationResponse createObservation(ObservationRequest request, Jwt jwt) {

        String userId = jwt.getClaim("id");
        String token = jwt.getTokenValue();

        Observation observation = Observation.builder()
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .observedAt(request.getObservedAt())
                .status(ObservationStatus.CREATED)
                .speciesId(null)
                .userId(userId)
                .build();

        observationRepository.save(observation);
        userClientService.discoverSpecies(token, observation.getSpeciesId()).subscribe();

        return mapEntityToDtoResponse(observation);

    }

    public List<ObservationResponse> getUserObservations(Jwt jwt) {

        String userId = jwt.getClaim("id");

        return observationRepository.findByUserId(userId)
                .stream()
                .map(ObservationMapper::mapEntityToDtoResponse)
                .toList();
    }

    public ObservationResponse getObservationById(Long id, Jwt jwt) {

        String userId = jwt.getClaim("id");
        String username = jwt.getClaim("username");

        return mapEntityToDtoResponse(observationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ObservationNotFoundException("No observation with id: "
                        + id + " found for username: " + username)));

    }


    public ResponseEntity<Void> updateObservationStatus(Long id, UpdateObservationStatusRequest body) {
        var obs = observationRepository.findById(id).orElseThrow();
        obs.setStatus(ObservationStatus.valueOf(body.getStatus()));
        obs.setUpdatedAt(LocalDateTime.now());

        obs.setSpecies(body.getSpecies());
        obs.setConfidence(body.getConfidence());
        obs.setSpeciesId(body.getSpeciesId());

        observationRepository.save(obs);
        return ResponseEntity.noContent().build();
    }

}
