package com.spring.dozen.hub.presentation.controller;

import com.spring.dozen.hub.application.dto.response.HubDetailResponseDto;
import com.spring.dozen.hub.application.dto.response.HubListResponseDto;
import com.spring.dozen.hub.application.dto.response.HubResponseDto;
import com.spring.dozen.hub.application.service.HubService;
import com.spring.dozen.hub.presentation.dto.ApiResponseDto;
import com.spring.dozen.hub.presentation.dto.HubRequestDto;
import com.spring.dozen.hub.presentation.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    @GetMapping
    public PageResponseDto<HubListResponseDto> getHubList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean isAsc,
            @RequestParam(required = false) String keyword
    ){
        Page<HubListResponseDto> hubPage = hubService.getHubList(page-1, size, sortBy, isAsc, keyword);

        return PageResponseDto.success(
                hubPage.getTotalPages(),
                hubPage.getNumber(),
                hubPage.getContent()
        );
    }

    // 허브 상세 조회
    @GetMapping("/{hudId}")
    public ApiResponseDto <HubDetailResponseDto> getHubDetail(@PathVariable UUID hubId) {
        HubDetailResponseDto response = hubService.getHubDetail(hubId);
        return ApiResponseDto.success(response);
    }

}
