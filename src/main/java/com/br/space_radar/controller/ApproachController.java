package com.br.space_radar.controller;

import com.br.space_radar.dto.ApproachResponseDTO;
import com.br.space_radar.dto.PageResponseDTO;
import com.br.space_radar.service.ApproachService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/v1/close-approach")
public class ApproachController {
    private final ApproachService service;

    public ApproachController(ApproachService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<ApproachResponseDTO>> findAll(
            @PageableDefault(size = 20, sort = "date") Pageable pageable) {
        return ResponseEntity.ok(service.findAllApproaches(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApproachResponseDTO> byId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.searchApproach(id));
    }

    @GetMapping("/asteroid/{asteroidId}")
    public ResponseEntity<PageResponseDTO<ApproachResponseDTO>> byAsteroid(
            @PathVariable UUID asteroidId,
            @PageableDefault(size = 20, sort = "date") Pageable pageable) {
        return ResponseEntity.ok(service.findApproachesByAsteroid(asteroidId, pageable));
    }
}

