package com.br.space_radar.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "nada_id", nullable = false, unique = true)
    private Long nasaId;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "diametro_min_m")
    private Double diametroMinM;

    @Column(name = "diametro_max_m")
    private Double diametroMaxM;

    @Column(name = "perigoso", nullable = false)
    private boolean perigoso;

    @Column(name = "url_jpl", length = 500)
    private String urlJpl;
}
