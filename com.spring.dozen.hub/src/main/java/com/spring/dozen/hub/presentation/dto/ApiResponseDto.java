package com.spring.dozen.hub.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 공통 DTO
 * */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponseDto<T> (
        String message,
        T data
) {
    public static <T> ApiResponseDto<T> success(T data) {
        return new ApiResponseDto<>("API 요청에 성공했습니다", data);
    }

    public static ApiResponseDto<Void> success() {
        return new ApiResponseDto<>("API 요청에 성공했습니다", null);
    }
}
