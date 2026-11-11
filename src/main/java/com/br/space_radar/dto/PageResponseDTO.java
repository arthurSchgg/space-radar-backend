package com.br.space_radar.dto;

import org.springframework.data.domain.Page;
import java.util.List;
import java.util.function.Function;

public record PageResponseDTO<T>(
        List<T> content, int page, int size, long totalElements, int totalPages
) {
    public static <S, T> PageResponseDTO<T> from(Page<S> page, Function<S, T> mapper) {
        return new PageResponseDTO<>(
                page.getContent().stream().map(mapper).toList(),
                page.getNumber(), page.getSize(), page.getTotalElements(),
                page.getTotalPages());
    }
}

