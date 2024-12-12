package com.spring.dozen.hub.application.exception;

public record ExceptionResponse(
        String message
) {
    // 아래 메서드는 HttpServletResponse 에서 json 형식으로 변환하기 위한 메서드로
    // Security 의 예외 처리기, CustomAuthenticationEntryPoint, CustomAccessDeniedHandler 에서
    // 사용되는 메서드로 일반적인 상황에서는 사용할 필요가 없습니다.
    public String toWrite() {
        return "{" +
                "\"message\":" + "\"" + message + "\"" +
                "}";
    }
}

