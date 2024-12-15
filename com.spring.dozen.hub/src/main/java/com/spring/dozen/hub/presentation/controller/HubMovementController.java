package com.spring.dozen.hub.presentation.controller;

import com.spring.dozen.hub.application.dto.response.*;
import com.spring.dozen.hub.application.service.HubMovementService;
import com.spring.dozen.hub.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    // 허브 이동정보 목록 조회
    @GetMapping
    public PageResponse<HubMovementListResponse> getHubMovementList(
            @SortDefault(
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            @RequestParam(required = false) String keyword
    ) {
        Page<HubMovementListResponse> hubMovementPage = hubMovementService.getHubMovementList(pageable, keyword);

        return PageResponse.success(
                hubMovementPage.getTotalPages(),
                hubMovementPage.getNumber(),
                hubMovementPage.getContent()
        );
    }

    // 허브 이동정보 상세 조회
    @GetMapping("/{hubMovementId}")
    public ApiResponse<HubMovementDetailResponse> getHubMovementDetail(@PathVariable UUID hubMovementId) {
        HubMovementDetailResponse response = hubMovementService.getHubMovementDetail(hubMovementId);
        return ApiResponse.success(response);
    }

    // 허브 수정
    @PutMapping("/{hubMovementId}")
    public ApiResponse<HubMovementDetailResponse> updateHubMovement(@PathVariable UUID hubMovementId,
                                                                    @RequestBody HubMovementRequest request) {
        HubMovementDetailResponse response = hubMovementService.updateHubMovement(hubMovementId, request.toDTO());;
        return ApiResponse.success(response);
    }

}
