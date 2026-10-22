package com.br.space_radar.controller;

import com.br.space_radar.dto.AsteroidRequestNasaDTO;
import com.br.space_radar.entity.Asteroid;
import com.br.space_radar.service.AsteroidService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/space-radar")
public class AsteroidController {

    private final AsteroidService service;

    public AsteroidController(AsteroidService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Asteroid>> listAll() {
        return ResponseEntity.ok(service.findAllAsteroids());
    }

    @GetMapping("/{nasaId}")
    public ResponseEntity<Asteroid> searchAsteroid(
            @PathVariable Long nasaId
    ) {
        return ResponseEntity.ok(service.searchAsteroid(nasaId));
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importFromNasa(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate){

        service.importFromNasa(startDate, endDate);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{nasaId}")
    public ResponseEntity<Asteroid> updateAsteroid(@PathVariable Long nasaId, @Valid @RequestBody AsteroidRequestNasaDTO dto) {
        return ResponseEntity.ok(service.updateAsteroid(nasaId, dto));
    }

    @DeleteMapping("/{nasaId}")
    public ResponseEntity<Void> deleteAsteroid(@PathVariable Long nasaId) {
        service.deleteAsteroid(nasaId);

        return ResponseEntity.noContent().build();
    }
}
