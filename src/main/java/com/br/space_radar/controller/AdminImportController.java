package com.br.space_radar.controller;

import com.br.space_radar.dto.ImportSummaryDTO;
import com.br.space_radar.service.AsteroidService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;

@RestController
@RequestMapping("/v1/admin")
public class AdminImportController {
    private final AsteroidService service;
    private final String adminToken;

    public AdminImportController(AsteroidService service,
                                 @Value("${radar.import.token:}") String adminToken) {
        this.service = service;
        this.adminToken = adminToken;
    }

    @PostMapping("/import")
    public ResponseEntity<ImportSummaryDTO> importFromNasa(
            @RequestHeader(value = "X-Admin-Token", required = false) String token,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (adminToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Administrative import is not configured");
        }
        if (token == null || !MessageDigest.isEqual(
                adminToken.getBytes(StandardCharsets.UTF_8),
                token.getBytes(StandardCharsets.UTF_8))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid import credentials");
        }
        return ResponseEntity.ok(service.importFromNasa(startDate, endDate));
    }
}

