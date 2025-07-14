package com.naturedex.species_service.repository;

import com.naturedex.species_service.entity.Species;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeciesRepository extends JpaRepository <Species, Long> {

}
