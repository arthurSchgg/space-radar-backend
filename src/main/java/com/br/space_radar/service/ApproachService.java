package com.br.space_radar.service;

import com.br.space_radar.dto.ApproachResponseDTO;
import com.br.space_radar.dto.PageResponseDTO;
import com.br.space_radar.repository.CloseApproachRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;

@Service
public class ApproachService {
    private final CloseApproachRepository repository;

    public ApproachService(CloseApproachRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public ApproachResponseDTO searchApproach(UUID id) {
        return repository.findById(id).map(ApproachResponseDTO::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Close approach not found"));
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<ApproachResponseDTO> findAllApproaches(Pageable pageable) {
        return PageResponseDTO.from(repository.findAllWithAsteroid(pageable), ApproachResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<ApproachResponseDTO> findApproachesByAsteroid(UUID asteroidId, Pageable pageable) {
        return PageResponseDTO.from(repository.findByAsteroidId(asteroidId, pageable), ApproachResponseDTO::from);
    }
}

