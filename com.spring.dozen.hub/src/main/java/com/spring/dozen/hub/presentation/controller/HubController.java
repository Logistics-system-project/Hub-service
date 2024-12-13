package com.spring.dozen.hub.presentation.controller;

import com.spring.dozen.hub.application.dto.response.HubDetailResponse;
import com.spring.dozen.hub.application.dto.response.HubListResponse;
import com.spring.dozen.hub.application.dto.response.HubResponse;
import com.spring.dozen.hub.application.service.HubService;
import com.spring.dozen.hub.presentation.dto.ApiResponse;
import com.spring.dozen.hub.presentation.dto.HubRequest;
import com.spring.dozen.hub.presentation.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub")
public class HubController {

    private final HubService hubService;

    // 허브 생성
    // 권한 추가 예정 : MASTER 관리자 권한만
    @PostMapping
    public ApiResponse<HubResponse> createHub(@RequestBody HubRequest request) {
        HubResponse response = hubService.createHub(request.toDTO());
        return ApiResponse.success(response);
    }

    // 허브 목록 조회
    @GetMapping
    public PageResponse<HubListResponse> getHubList(
            @SortDefault(
                    sort = {"created_at"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            @RequestParam(required = false) String keyword
    ) {
        Page<HubListResponse> hubPage = hubService.getHubList(pageable, keyword);

        return PageResponse.success(
                hubPage.getTotalPages(),
                hubPage.getNumber(),
                hubPage.getContent()
        );
    }

    // 허브 상세 조회
    @GetMapping("/{hubId}")
    public ApiResponse<HubDetailResponse> getHubDetail(@PathVariable UUID hubId) {
        HubDetailResponse response = hubService.getHubDetail(hubId);
        return ApiResponse.success(response);
    }

    // 허브 수정
    @PutMapping("/{hubId}")
    public ApiResponse<HubDetailResponse> updateHub(@PathVariable UUID hubId,
                                                    @RequestBody HubRequest request) {
        HubDetailResponse response = hubService.updateHub(hubId, request.toDTO());;
        return ApiResponse.success(response);
    }

    // 허브 삭제
    @DeleteMapping("/{hubId}")
    public ApiResponse<Void> deleteHub(
            @PathVariable UUID hubId
    ){
        hubService.deleteHub(hubId);
        return ApiResponse.success();
    }
}
