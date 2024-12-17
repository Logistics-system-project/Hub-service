package com.spring.dozen.hub.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // AddressToCoordinateService
    KAKAO_API_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "API 요청 실패"),
    KAKAO_INVALID_API_KEY(HttpStatus.UNAUTHORIZED, "API 키가 유효하지 않습니다"),
    KAKAO_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류 발생"),
    NOT_FOUND_ADDRESS(HttpStatus.NOT_FOUND, "주소 정보를 찾을 수 없습니다."),

    // 허브
    NOT_FOUND_HUB(HttpStatus.NOT_FOUND, "해당 허브를 찾을 수 없습니다."),
    // 허브 이동정보
    NOT_FOUND_HUBMOVEMENT(HttpStatus.NOT_FOUND, "해당 허브 이동 정보를 찾을 수 없습니다."),

    // 권한
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    MISSING_ROLE(HttpStatus.BAD_REQUEST, "권한 정보가 없습니다."),
    ;



    private final HttpStatus httpStatus;
    private final String message;
}
