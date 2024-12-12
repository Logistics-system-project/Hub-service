package com.spring.dozen.hub.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * 공통 DTO
 * */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponseDto<T> (
        int totalPages,
        int pageNumber,
        List<T> content
) {
    public static <T> PageResponseDto<T> success(int totalPages, int pageNumber, List<T> content) {
        return new PageResponseDto<>(totalPages, pageNumber, content);
    }

    public static PageResponseDto<Void> success(int totalPages, int pageNumber) {
        return new PageResponseDto<>(totalPages, pageNumber, null);
    }
}
