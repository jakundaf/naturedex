package com.naturedex.user_service.repository;

import com.naturedex.user_service.entity.DiscoveredSpecies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DiscoveredSpeciesRepository extends JpaRepository <DiscoveredSpecies, Long> {

    List<DiscoveredSpecies> findAllByUserId(String userId);

    boolean existsByUserIdAndSpeciesId(String userId, Long speciesId);

}
