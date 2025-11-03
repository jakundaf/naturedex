package com.naturedex.recognition_worker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecognitionResult {

    private String species;
    private double confidence;
    private Long speciesId;
}
