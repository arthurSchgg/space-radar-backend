package com.br.space_radar.service;

import com.br.space_radar.client.NasaClient;
import com.br.space_radar.dto.*;
import com.br.space_radar.entity.Asteroid;
import com.br.space_radar.entity.CloseApproach;
import com.br.space_radar.mapper.ApproachMapper;
import com.br.space_radar.mapper.AsteroidMapper;
import com.br.space_radar.repository.AsteroidRepository;
import com.br.space_radar.repository.CloseApproachRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AsteroidService {
    private final AsteroidRepository asteroids;
    private final AsteroidMapper asteroidMapper;
    private final NasaClient nasaClient;
    private final ApproachMapper approachMapper;
    private final CloseApproachRepository approaches;

    public AsteroidService(AsteroidRepository asteroids, AsteroidMapper asteroidMapper,
                           NasaClient nasaClient, ApproachMapper approachMapper,
                           CloseApproachRepository approaches) {
        this.asteroids = asteroids;
        this.asteroidMapper = asteroidMapper;
        this.nasaClient = nasaClient;
        this.approachMapper = approachMapper;
        this.approaches = approaches;
    }

    @Transactional(readOnly = true)
    public AsteroidResponseDTO searchAsteroid(Long nasaId) {
        return asteroids.findByNasaId(nasaId).map(AsteroidResponseDTO::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Asteroid not found"));
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<AsteroidResponseDTO> findAllAsteroids(
            String search, boolean hazardousOnly, Pageable pageable) {
        String term = search == null || search.isBlank() ? null : search.trim();
        return PageResponseDTO.from(
                asteroids.filter(term, hazardousOnly, pageable), AsteroidResponseDTO::from);
    }

    @Transactional
    public ImportSummaryDTO importFromNasa(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || endDate.isBefore(startDate)
                || ChronoUnit.DAYS.between(startDate, endDate) > 7) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "NASA feed supports intervals of up to 7 days");
        }
        NasaFeedResponseDTO response = nasaClient.searchAsteroids(startDate, endDate);
        if (response == null || response.getNearEarthObjects() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "NASA returned an invalid feed response");
        }
        int created = 0, updated = 0, approachesCreated = 0, approachesUpdated = 0;
        for (List<AsteroidRequestNasaDTO> items : response.getNearEarthObjects().values()) {
            if (items == null) continue;
            for (AsteroidRequestNasaDTO dto : items) {
                if (dto == null || dto.nasaId() == null || dto.name() == null
                        || dto.estimatedDiameter() == null || dto.estimatedDiameter().meters() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                            "NASA returned incomplete asteroid data");
                }
                var existing = asteroids.findByNasaId(dto.nasaId());
                Asteroid asteroid;
                if (existing.isPresent()) {
                    asteroid = existing.get();
                    updated++;
                } else {
                    asteroid = asteroidMapper.toEntity(dto);
                    created++;
                }
                asteroidMapper.updateEntity(asteroid, dto);
                asteroid = asteroids.save(asteroid);
                if (dto.closeApproachData() == null) continue;
                for (var approachDto : dto.closeApproachData()) {
                    CloseApproach incoming = approachMapper.toEntity(approachDto, asteroid);
                    var match = approaches.findByAsteroidIdAndDate(asteroid.getId(), incoming.getDate());
                    if (match.isPresent()) {
                        CloseApproach stored = match.get();
                        stored.setVelocityKmS(incoming.getVelocityKmS());
                        stored.setDistanceKm(incoming.getDistanceKm());
                        stored.setLunarDistance(incoming.getLunarDistance());
                        approaches.save(stored);
                        approachesUpdated++;
                    } else {
                        approaches.save(incoming);
                        approachesCreated++;
                    }
                }
            }
        }
        return new ImportSummaryDTO(startDate, endDate,
                created, updated, approachesCreated, approachesUpdated);
    }
}

