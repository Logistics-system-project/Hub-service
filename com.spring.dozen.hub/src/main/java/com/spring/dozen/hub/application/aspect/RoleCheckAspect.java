package com.spring.dozen.hub.application.aspect;

import com.spring.dozen.hub.application.annotation.RequireRole;
import com.spring.dozen.hub.application.exception.ErrorCode;
import com.spring.dozen.hub.application.exception.HubException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RoleCheckAspect {
    private final HttpServletRequest request;
    @Before("@annotation(requireRole)")
    public void checkRole(RequireRole requireRole) {
        // 헤더에서 role 정보 가져오기
        String role = request.getHeader("X-Role");
        if (role == null) {
            throw new HubException(ErrorCode.MISSING_ROLE);
        }
        // 허용된 권한 체크
        List<String> allowedRoles = List.of(requireRole.value());
        if (!allowedRoles.contains(role)) {
            throw new HubException(ErrorCode.ACCESS_DENIED);
        }
    }
}
