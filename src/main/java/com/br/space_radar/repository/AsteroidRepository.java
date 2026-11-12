package com.br.space_radar.repository;

import com.br.space_radar.entity.Asteroid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

public interface AsteroidRepository extends JpaRepository<Asteroid, UUID> {
    Optional<Asteroid> findByNasaId(Long nasaId);
    boolean existsByNasaId(Long nasaId);

    @Query("""
            SELECT a FROM Asteroid a
            WHERE (:search IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')))
              AND (:hazardousOnly = false OR a.dangerous = true)
            """)
    Page<Asteroid> filter(@Param("search") String search,
                          @Param("hazardousOnly") boolean hazardousOnly,
                          Pageable pageable);
}

