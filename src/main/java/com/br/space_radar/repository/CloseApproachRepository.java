package com.br.space_radar.repository;

import com.br.space_radar.entity.CloseApproach;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CloseApproachRepository extends JpaRepository<CloseApproach, UUID> {

    List<CloseApproach> findByAsteroidId(UUID asteroidId);

    List<CloseApproach> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    boolean existsByAsteroidIdAndDate(UUID asteroid, LocalDateTime date);

    @EntityGraph(attributePaths = "asteroid")
    @Query("SELECT ca FROM CloseApproach ca")
    List<CloseApproach> findAllWithAsteroid();
}
