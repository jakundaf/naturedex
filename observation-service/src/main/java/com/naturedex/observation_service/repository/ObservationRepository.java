package com.naturedex.observation_service.repository;

import com.naturedex.observation_service.entity.Observation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ObservationRepository extends JpaRepository<Observation, Long> {

    Optional<Observation> findByUserId(String userId);
    Optional<Observation> findByIdAndUserId(Long id, String userId);
}
