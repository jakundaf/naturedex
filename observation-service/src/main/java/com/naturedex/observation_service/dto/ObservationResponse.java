package com.naturedex.observation_service.dto;

import com.naturedex.observation_service.utils.ObservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObservationResponse {

    private Long id;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private LocalDateTime observedAt;
    private ObservationStatus status;
    private String speciesName;
}
