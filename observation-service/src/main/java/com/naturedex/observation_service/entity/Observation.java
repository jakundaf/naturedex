package com.naturedex.observation_service.entity;

import com.naturedex.observation_service.utils.ObservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private String imageUrl;

    private Double latitude;
    private Double longitude;

    private LocalDateTime observedAt;

    @Enumerated(EnumType.STRING)
    private ObservationStatus status;

    private String speciesName;

    @Column(nullable = false)
    private String username;

}
