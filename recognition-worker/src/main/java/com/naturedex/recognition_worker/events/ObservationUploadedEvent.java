package com.naturedex.recognition_worker.events;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.time.LocalDateTime;

public record ObservationUploadedEvent(
        Long observationId,
        String userId,
        String objectKey,
        String contentType,
        Long fileSizeBytes,
        Double latitude,
        Double longitude,
        LocalDateTime observedAt,
        Instant uploadedAt,
        String jwt
) {}

