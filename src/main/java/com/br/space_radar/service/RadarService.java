package com.br.space_radar.service;

import com.br.space_radar.dto.*;
import com.br.space_radar.repository.CloseApproachRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class RadarService {
    private final CloseApproachRepository repository;

    public RadarService(CloseApproachRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public RadarResponseDTO read(LocalDate start, LocalDate end,
                                 boolean hazardousOnly, Double maxDistanceKm,
                                 int page, int size) {
        if (start == null || end == null || end.isBefore(start) ||
                ChronoUnit.DAYS.between(start, end) > 365) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Choose a valid date interval of at most 365 days");
        }
        if (maxDistanceKm != null &&
                (!Double.isFinite(maxDistanceKm) || maxDistanceKm <= 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "maxDistanceKm must be a positive finite number");
        }
        if (page < 0 || size < 1 || size > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Page must be nonnegative and size between 1 and 100");
        }
        var result = repository.findRadarEvents(start.atStartOfDay(),
                end.plusDays(1).atStartOfDay(), hazardousOnly, maxDistanceKm,
                PageRequest.of(page, size, Sort.by(Sort.Order.asc("date"), Sort.Order.asc("id"))));
        return new RadarResponseDTO(
                "SCHEMATIC_DISTANCE_ONLY", false, "km", "km/s", start, end,
                PageResponseDTO.from(result, ApproachResponseDTO::from));
    }
}

