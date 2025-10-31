package com.naturedex.recognition_worker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateObservationStatusRequest {
    private String status;
    private String species;
    private Double confidence;
    private Long speciesId;
}