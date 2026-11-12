package com.br.space_radar.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_close_approach",
        uniqueConstraints = @UniqueConstraint(name = "uk_approach_asteroid_date",
                columnNames = {"asteroid_id", "date_approach"}),
        indexes = {
                @Index(name = "idx_approach_date", columnList = "date_approach"),
                @Index(name = "idx_approach_asteroid", columnList = "asteroid_id")
        })
public class CloseApproach {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asteroid_id", nullable = false)
    private Asteroid asteroid;

    // NASA's close_approach_date_full represents UTC, stored as UTC local date-time.
    @Column(name = "date_approach", nullable = false)
    private LocalDateTime date;

    @Column(name = "velocity_kms")
    private Double velocityKmS;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "lunar_distance")
    private Double lunarDistance;
}

