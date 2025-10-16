package com.naturedex.observation_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PresignedUrlRequest(
        @NotBlank String contentType,
        @NotNull Long fileSize,
        Double lat,
        Double lng,
        LocalDateTime takenAt
) {}