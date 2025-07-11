package com.naturedex.observation_service.repository;

import com.naturedex.observation_service.entity.Observation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ObservationRepository extends JpaRepository<Observation, Long> {

    Optional<Observation> findByUsername(String username);
    Optional<Observation> findByIdAndUsername(Long id, String username);
}
