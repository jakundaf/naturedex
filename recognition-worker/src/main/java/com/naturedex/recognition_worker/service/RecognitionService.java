package com.naturedex.recognition_worker.service;

import com.naturedex.recognition_worker.dto.RecognitionResult;
import com.naturedex.recognition_worker.dto.UpdateObservationStatusRequest;
import com.naturedex.recognition_worker.events.ObservationUploadedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;

@Slf4j
@Service
public class RecognitionService {

    private final RestClient http = RestClient.create();
    @Value("${recognition.observation-service-base-url}")
    private String observationServiceBaseUrl;

    @Value("${discovered-species.user-service-base-url}")
    private String userServiceBaseUrl;



    public RecognitionResult recognize(ObservationUploadedEvent event) {
        return new RecognitionResult("Papilio machaon", 0.93, 1L);





//        ObservationUploadedEvent event = new ObservationUploadedEvent(
//                observation.getId(),
//                observation.getUserId(),
//                observation.getObjectKey(),
//                observation.getContentType(),
//                observation.getFileSizeBytes(),
//                observation.getLatitude(),
//                observation.getLongitude(),
//                observation.getObservedAt(),
//                Instant.now(),
//                jwt.getTokenValue()
//        );
    }


    public void updateObservationStatus(String jwt, Long observationId, String species, double confidence, Long speciesId) {

        UpdateObservationStatusRequest request = UpdateObservationStatusRequest.builder()
                .species(species)
                .confidence(confidence)
                .status("RECOGNIZED")
                .speciesId(speciesId)
                .build();

        http.patch()
                .uri(observationServiceBaseUrl + "/api/observations/{id}/status", observationId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .body(request)
                .retrieve()
                .toBodilessEntity();

        log.info("RecognitionService - observation status updated to RECOGNIZED");

        http.post()
                .uri(userServiceBaseUrl + "/api/users/me/discovered/{speciesId}", speciesId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .retrieve()
                .toBodilessEntity();

        log.info("RecognitionService - specie discovered for certain user");
    }

    public void publishRecognizedEvent(Long observationId, RecognitionResult result) {

    }

}
