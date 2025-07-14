package com.naturedex.species_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "species")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String latinName;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String description;

    private String habitat;

    private boolean isVisible = true;

    public enum Category {
        ANIMAL,
        PLANT,
        FUNGUS
    }
}
