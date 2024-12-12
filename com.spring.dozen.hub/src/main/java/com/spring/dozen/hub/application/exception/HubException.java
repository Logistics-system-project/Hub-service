package com.spring.dozen.hub.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HubException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;

    public HubException(ErrorCode errorCode) {
        this.httpStatus = errorCode.getHttpStatus();
        this.message = errorCode.getMessage();
    }
}
