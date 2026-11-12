package com.br.space_radar.repository;

import com.br.space_radar.entity.CloseApproach;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface CloseApproachRepository extends JpaRepository<CloseApproach, UUID> {
    @EntityGraph(attributePaths = "asteroid")
    Optional<CloseApproach> findById(UUID id);

    @EntityGraph(attributePaths = "asteroid")
    @Query("SELECT c FROM CloseApproach c")
    Page<CloseApproach> findAllWithAsteroid(Pageable pageable);

    @EntityGraph(attributePaths = "asteroid")
    Page<CloseApproach> findByAsteroidId(UUID asteroidId, Pageable pageable);

    Optional<CloseApproach> findByAsteroidIdAndDate(UUID asteroidId, LocalDateTime date);

    @EntityGraph(attributePaths = "asteroid")
    @Query(value = """
            SELECT c FROM CloseApproach c JOIN c.asteroid a
            WHERE c.date >= :start AND c.date < :end
              AND (:hazardousOnly = false OR a.dangerous = true)
              AND (:maxDistanceKm IS NULL OR c.distanceKm <= :maxDistanceKm)
            """,
            countQuery = """
            SELECT COUNT(c) FROM CloseApproach c JOIN c.asteroid a
            WHERE c.date >= :start AND c.date < :end
              AND (:hazardousOnly = false OR a.dangerous = true)
              AND (:maxDistanceKm IS NULL OR c.distanceKm <= :maxDistanceKm)
            """)
    Page<CloseApproach> findRadarEvents(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end,
                                       @Param("hazardousOnly") boolean hazardousOnly,
                                       @Param("maxDistanceKm") Double maxDistanceKm,
                                       Pageable pageable);
}

