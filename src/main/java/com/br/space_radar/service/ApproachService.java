package com.br.space_radar.service;

import com.br.space_radar.client.NasaClient;
import com.br.space_radar.dto.AsteroidRequestNasaDTO;
import com.br.space_radar.dto.NasaFeedResponseDTO;
import com.br.space_radar.entity.Asteroid;
import com.br.space_radar.entity.CloseApproach;
import com.br.space_radar.mapper.ApproachMapper;
import com.br.space_radar.mapper.AsteroidMapper;
import com.br.space_radar.repository.AsteroidRepository;
import com.br.space_radar.repository.CloseApproachRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApproachService {

    private final CloseApproachRepository closeApproachRepository;
    private final NasaClient nasaClient;
    private final AsteroidMapper asteroidMapper;
    private final AsteroidRepository asteroidRepository;

    public ApproachService(CloseApproachRepository closeApproachRepository, NasaClient nasaClient, AsteroidMapper asteroidMapper, AsteroidRepository asteroidRepository) {
        this.closeApproachRepository = closeApproachRepository;
        this.nasaClient = nasaClient;
        this.asteroidMapper = asteroidMapper;
        this.asteroidRepository = asteroidRepository;
    }

    @Transactional(readOnly = true)
    public Optional<CloseApproach> searchApproach(UUID id) {
        return closeApproachRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CloseApproach> findAllApproaches() {
        return closeApproachRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<CloseApproach> findApproachesByAsteroid(UUID asteroidId) {
        return closeApproachRepository.findByAsteroidId(asteroidId);
    }

    @Transactional(readOnly = true)
    public List<CloseApproach> findApproachesByDateRange(LocalDateTime start, LocalDateTime end) {
        return closeApproachRepository.findByDateBetween(start, end);
    }

    @Transactional
    public CloseApproach saveApproach(CloseApproach approach) {
        return closeApproachRepository.save(approach);
    }

    @Transactional
    public void deleteApproach(UUID id) {
        CloseApproach approach = closeApproachRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Approach not found with id: " + id)
                );

        closeApproachRepository.delete(approach);
    }
}
