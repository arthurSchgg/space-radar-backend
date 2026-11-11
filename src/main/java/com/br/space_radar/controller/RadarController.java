package com.br.space_radar.controller;

import com.br.space_radar.dto.RadarResponseDTO;
import com.br.space_radar.service.RadarService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/v1/radar")
public class RadarController {
    private final RadarService service;

    public RadarController(RadarService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<RadarResponseDTO> events(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "false") boolean hazardousOnly,
            @RequestParam(required = false) Double maxDistanceKm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        LocalDate start = startDate == null ? LocalDate.now(java.time.Clock.systemUTC()) : startDate;
        LocalDate end = endDate == null ? start.plusDays(7) : endDate;
        return ResponseEntity.ok(service.read(
                start, end, hazardousOnly, maxDistanceKm, page, size));
    }
}

