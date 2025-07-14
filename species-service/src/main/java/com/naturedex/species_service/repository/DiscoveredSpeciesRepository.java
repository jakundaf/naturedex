package com.naturedex.species_service.repository;

import com.naturedex.species_service.entity.DiscoveredSpecies;
import com.naturedex.species_service.entity.Species;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiscoveredSpeciesRepository extends JpaRepository <DiscoveredSpecies, Long> {

    List<DiscoveredSpecies> findByUsername(String username);

    Optional<DiscoveredSpecies> findByUsernameAndSpecies(String username, Species species);

    boolean existsByUsernameAndSpecies(String username, Species species);

}
