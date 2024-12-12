package com.spring.dozen.hub.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * 공통 DTO
 * */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponse<T> (
        int totalPages,
        int pageNumber,
        List<T> content
) {
    public static <T> PageResponse<T> success(int totalPages, int pageNumber, List<T> content) {
        return new PageResponse<>(totalPages, pageNumber, content);
    }

    public static PageResponse<Void> success(int totalPages, int pageNumber) {
        return new PageResponse<>(totalPages, pageNumber, null);
    }
}
