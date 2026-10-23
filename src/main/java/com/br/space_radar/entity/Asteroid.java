package com.br.space_radar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_asteroid")
public class Asteroid {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nasa_id", nullable = false, unique = true)
    private Long nasaId;

    @JsonIgnore
    @OneToMany(mappedBy = "asteroid", fetch = FetchType.LAZY)
    private List<CloseApproach> closeApproaches;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "min_diameter_m")
    private Double minDiameterM;

    @Column(name = "max_diameter_m")
    private Double maxDiameterM;

    @Column(name = "dangerous", nullable = false)
    private boolean dangerous;

    @Column(name = "url_jpl", length = 500)
    private String urlJpl;
}
