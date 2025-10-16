package com.naturedex.observation_service.dto;


import java.time.LocalDateTime;

public record PresignedUrlResponse(
        Long observationId,
        String uploadUrl,
        String objectKey,
        LocalDateTime expiresAt
) {}
