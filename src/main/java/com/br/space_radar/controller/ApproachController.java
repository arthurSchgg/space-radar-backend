package com.br.space_radar.controller;

import com.br.space_radar.entity.CloseApproach;
import com.br.space_radar.service.ApproachService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/close-approach")
public class ApproachController {

    private final ApproachService service;

    public ApproachController(ApproachService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CloseApproach>> findAllApproaches(){
        return ResponseEntity.ok(service.findAllApproaches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<CloseApproach>> searchApproach(@PathVariable UUID id){
        return ResponseEntity.ok(service.searchApproach(id));
    }

    @GetMapping("/asteroid/{asteroidId}")
    public ResponseEntity<List<CloseApproach>> findApproachesByAsteroid(@PathVariable UUID asteroidId){
        return ResponseEntity.ok(service.findApproachesByAsteroid(asteroidId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<CloseApproach>> findApproachesByDateRange(@RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(service.findApproachesByDateRange(start, end));
    }

    @PutMapping
    public ResponseEntity<CloseApproach> saveApproach(@RequestBody CloseApproach approach){
        return ResponseEntity.ok(service.saveApproach(approach));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApproach(@PathVariable UUID id) {
        service.deleteApproach(id);

        return ResponseEntity.noContent().build();
    }
}
