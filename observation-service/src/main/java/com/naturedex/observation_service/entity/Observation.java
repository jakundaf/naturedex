package com.naturedex.observation_service.entity;

import com.naturedex.observation_service.utils.ObservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "observations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contentType;
    private Long fileSizeBytes;


    private Double latitude;
    private Double longitude;

    private LocalDateTime observedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private ObservationStatus status;

    private Long speciesId;
    private String species;
    private Double confidence;
    private String recognizer;

    @Column(nullable = false)
    private String userId;

    private String objectKey;

}
