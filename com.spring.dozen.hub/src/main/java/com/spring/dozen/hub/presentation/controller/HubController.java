package com.spring.dozen.hub.presentation.controller;

import com.spring.dozen.hub.application.dto.response.HubResponseDto;
import com.spring.dozen.hub.application.service.HubService;
import com.spring.dozen.hub.presentation.dto.ApiResponseDto;
import com.spring.dozen.hub.presentation.dto.HubRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub")
public class HubController {

    private final HubService hubService;

    // 허브 생성
    // 권한 추가 예정 : MASTER 관리자 권한만
    @PostMapping
    public ApiResponseDto <HubResponseDto> createHub(@RequestBody HubRequestDto request) {
        HubResponseDto response = hubService.createHub(request.toDTO());
        return ApiResponseDto.success(response);
    }

    // 허브 목록 조회



    // 허브 상세 조회

}
