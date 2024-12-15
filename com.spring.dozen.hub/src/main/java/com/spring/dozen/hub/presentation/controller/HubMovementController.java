package com.spring.dozen.hub.presentation.controller;

import com.spring.dozen.hub.application.dto.response.HubMovementResponse;
import com.spring.dozen.hub.application.service.HubMovementService;
import com.spring.dozen.hub.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub-movement")
public class HubMovementController {

    private final HubMovementService hubMovementService;

    // 허브 이동정보 생성
    // 권한 추가 예정 : MASTER 관리자 권한만
    @PostMapping
    public ApiResponse<HubMovementResponse> createHubMovement(@RequestBody HubMovementRequest request) {
        HubMovementResponse response = hubMovementService.createHubMovement(request.toDTO());
        return ApiResponse.success(response);
    }

}
