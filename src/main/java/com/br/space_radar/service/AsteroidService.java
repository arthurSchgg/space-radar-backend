package com.br.space_radar.service;

import com.br.space_radar.dto.AsteroidRequestNasaDTO;
import com.br.space_radar.mapper.AsteroidMapper;
import com.br.space_radar.entity.Asteroid;
import com.br.space_radar.repository.AsteroidRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsteroidService {

    private final AsteroidRepository asteroidRepository;
    private final AsteroidMapper asteroidMapper;

    public AsteroidService(AsteroidRepository asteroidRepository, AsteroidMapper asteroidMapper) {
        this.asteroidRepository = asteroidRepository;
        this.asteroidMapper = asteroidMapper;
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
