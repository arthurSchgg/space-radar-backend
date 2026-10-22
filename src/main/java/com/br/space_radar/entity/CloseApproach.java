package com.br.space_radar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_close_approach")
public class CloseApproach {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asteroid_id")
    private Asteroid asteroid;

    @Column(name = "date_approach", nullable = false)
    private LocalDateTime date;

    @Column(name = "velocity_kms")
    private Double velocityKmS;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "lunar_distance")
    private Double lunarDistance;
}
