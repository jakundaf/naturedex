package com.naturedex.observation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObservationRequest {

    private Double latitude;
    private Double longitude;
    private LocalDateTime observedAt;
}

