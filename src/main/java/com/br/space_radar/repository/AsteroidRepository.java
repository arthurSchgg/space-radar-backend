package com.br.space_radar.repository;

import com.br.space_radar.entity.Asteroid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AsteroidRepository extends JpaRepository<Asteroid, UUID> {

    Optional<Asteroid> findByNasaId(Long nasaId);

    boolean existsByNasaId(Long nasaId);
}
