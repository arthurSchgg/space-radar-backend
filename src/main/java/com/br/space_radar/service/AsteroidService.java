package com.br.space_radar.service;

import com.br.space_radar.client.NasaClient;
import com.br.space_radar.dto.AsteroidRequestNasaDTO;
import com.br.space_radar.dto.NasaFeedResponseDTO;
import com.br.space_radar.entity.CloseApproach;
import com.br.space_radar.mapper.ApproachMapper;
import com.br.space_radar.mapper.AsteroidMapper;
import com.br.space_radar.entity.Asteroid;
import com.br.space_radar.repository.AsteroidRepository;
import com.br.space_radar.repository.CloseApproachRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AsteroidService {

    private final AsteroidRepository asteroidRepository;
    private final AsteroidMapper asteroidMapper;
    private final NasaClient nasaClient;
    private final ApproachMapper approachMapper;
    private final CloseApproachRepository closeApproachRepository;

    public AsteroidService(AsteroidRepository asteroidRepository, AsteroidMapper asteroidMapper, NasaClient nasaClient, ApproachMapper approachMapper, CloseApproachRepository closeApproachRepository) {
        this.asteroidRepository = asteroidRepository;
        this.asteroidMapper = asteroidMapper;
        this.nasaClient = nasaClient;
        this.approachMapper = approachMapper;
        this.closeApproachRepository = closeApproachRepository;
    }

    @Transactional(readOnly = true)
    public Asteroid searchAsteroid(Long nasaId) {

        return asteroidRepository.findByNasaId(nasaId)
                .orElseThrow(() -> new RuntimeException("Asteroid not found with this nasaId: " + nasaId)
                );
    }

    @Transactional(readOnly = true)
    public List<Asteroid> findAllAsteroids(){
        return asteroidRepository.findAll();
    }

    @Transactional
    public void importFromNasa(LocalDate startDate, LocalDate endDate) {
        NasaFeedResponseDTO response =
                nasaClient.searchAsteroids(startDate, endDate);

        for (List<AsteroidRequestNasaDTO> asteroids :
                response.getNearEarthObjects().values()) {

            for (AsteroidRequestNasaDTO dto : asteroids) {
                Asteroid asteroid = asteroidRepository.findByNasaId(dto.nasaId())
                        .orElseGet(() -> asteroidMapper.toEntity(dto));

                asteroidMapper.updateEntity(asteroid, dto);
                asteroidRepository.save(asteroid);

                for(var approachDto : dto.closeApproachData()) {
                    CloseApproach approach = approachMapper.toEntity(approachDto, asteroid);

                    boolean exists = closeApproachRepository
                            .existsByAsteroidIdAndDate(
                                    asteroid.getId(),
                                    approach.getDate()
                            );

                    if(!exists){
                        closeApproachRepository.save(approach);
                    }
                }
            }
        }
    }

    @Transactional
    public Asteroid saveAsteroid(Asteroid asteroid){
        return asteroidRepository.save(asteroid);
    }

    @Transactional
    public Asteroid updateAsteroid(Long nasaId, AsteroidRequestNasaDTO dto){

        Asteroid asteroid = searchAsteroid(nasaId);

        asteroidMapper.updateEntity(asteroid, dto);

        return asteroidRepository.save(asteroid);
    }

    @Transactional
    public void deleteAsteroid(Long nasaId){
        Asteroid asteroid = searchAsteroid(nasaId);
        asteroidRepository.delete(asteroid);
    }
}
