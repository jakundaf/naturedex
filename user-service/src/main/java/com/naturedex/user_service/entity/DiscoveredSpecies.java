package com.naturedex.user_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "discovered_species", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "species_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscoveredSpecies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "species_id", nullable = false)
    private Long speciesId;

    @Column(name = "discovered_at", nullable = false)
    private LocalDateTime discoveredAt;

}
