package com.br.space_radar.controller;

import com.br.space_radar.dto.AsteroidResponseDTO;
import com.br.space_radar.dto.PageResponseDTO;
import com.br.space_radar.service.AsteroidService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/space-radar")
public class AsteroidController {
    private final AsteroidService service;

    public AsteroidController(AsteroidService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<AsteroidResponseDTO>> listAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "false") boolean hazardousOnly,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(service.findAllAsteroids(search, hazardousOnly, pageable));
    }

    @GetMapping("/{nasaId}")
    public ResponseEntity<AsteroidResponseDTO> searchAsteroid(@PathVariable Long nasaId) {
        return ResponseEntity.ok(service.searchAsteroid(nasaId));
    }
}

