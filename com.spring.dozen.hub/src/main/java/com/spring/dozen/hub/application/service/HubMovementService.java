package com.spring.dozen.hub.application.service;

import com.spring.dozen.hub.application.dto.HubMovementCreate;
import com.spring.dozen.hub.application.dto.HubMovementUpdate;
import com.spring.dozen.hub.application.dto.response.*;
import com.spring.dozen.hub.application.exception.ErrorCode;
import com.spring.dozen.hub.application.exception.HubException;
import com.spring.dozen.hub.domain.entity.Hub;
import com.spring.dozen.hub.domain.entity.HubMovement;
import com.spring.dozen.hub.domain.repository.HubMovementRepository;
import com.spring.dozen.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubMovementService {

    private final HubRepository hubRepository;
    private final HubMovementRepository hubMovementRepository;

    private final GeminiService geminiService;

    // 허브 이동정보 생성
    @Transactional
    public HubMovementResponse createHubMovement(HubMovementCreate request){
        // 출발 허브
        Hub departureHub = findValidHub(request.departureHubId());
        // 도착 허브
        Hub arrivalHub = findValidHub(request.arrivalHubId());

        // 출발 허브의 중앙 허브
        Hub centralDepartureHub = findCentralHub(departureHub);

        // 도착 허브의 중앙 허브 정보
        Hub centralArrivalHub = findCentralHub(arrivalHub);

        // 이동 경로 주소
        List<String> addresses = buildRouteAddresses(departureHub, centralDepartureHub, centralArrivalHub, arrivalHub);

        // Gemini API 호출
         String response = geminiService.callGeminiAPI(addresses);

        // Gemini 응답에서 거리 및 시간 추출
        int totalTime = geminiService.extractTotalTime(response);
        int totalDistance = geminiService.extractTotalDistance(response);

        HubMovement hubMovement = HubMovement.create(
                departureHub,
                arrivalHub,
                totalTime,
                totalDistance
        );
        hubMovementRepository.save(hubMovement);

        return HubMovementResponse.from(hubMovement);
    }

    // 허브 이동정보 목록 조회
    @Transactional
    public Page<HubMovementListResponse> getHubMovementList(Pageable pageable, String keyword){
        // size 값 조정
        int validatedSize = (pageable.getPageSize() == 10 || pageable.getPageSize() == 30 || pageable.getPageSize() == 50)
                ? pageable.getPageSize()
                : 10;

        // 새로운 Pageable 생성
        Pageable validatedPageable = PageRequest.of(
                pageable.getPageNumber(),
                validatedSize,
                pageable.getSort()
        );

        Page<HubMovement> hubMovementPage = hubMovementRepository.findByKeyword(keyword, validatedPageable);

        return hubMovementPage.map(HubMovementListResponse::from);
    }

    // 허브 이동정보 상세 조회
    @Transactional
    public HubMovementDetailResponse getHubMovementDetail(UUID hubMovementId){
        HubMovement hubMovement = hubMovementRepository.findByHubMovementIdAndIsDeletedFalse(hubMovementId)
                .orElseThrow(() -> new HubException(ErrorCode.NOT_FOUND_HUBMOVEMENT));
        return HubMovementDetailResponse.from(hubMovement);
    }

    // 허브 이동정보 수정
    @Transactional
    public HubMovementDetailResponse updateHubMovement(UUID hubMovementId, HubMovementUpdate request){
        HubMovement hubMovement = hubMovementRepository.findByHubMovementIdAndIsDeletedFalse(hubMovementId)
                .orElseThrow(() -> new HubException(ErrorCode.NOT_FOUND_HUBMOVEMENT));

            // 업데이트
            hubMovement.update(
                    request.time(),
                    request.distance()
            );

        return HubMovementDetailResponse.from(hubMovement);
    }

    private Hub findValidHub(UUID hubId) {
        return hubRepository.findByHubIdAndIsDeletedFalse(hubId)
                .orElseThrow(() -> new HubException(ErrorCode.NOT_FOUND_HUB));
    }

    private Hub findCentralHub(Hub hub) {
        if (hub.getCentralHubId() != null) {
            return hubRepository.findByHubIdAndIsDeletedFalse(hub.getCentralHubId())
                    .orElseThrow(() -> new HubException(ErrorCode.NOT_FOUND_HUB));
        }
        return null;
    }

    private List<String> buildRouteAddresses(Hub departureHub, Hub centralDepartureHub, Hub centralArrivalHub, Hub arrivalHub) {
        List<String> addresses = new ArrayList<>();
        addresses.add(departureHub.getAddress());

        if (centralDepartureHub != null) {
            addresses.add(centralDepartureHub.getAddress());
        }

        if (centralArrivalHub != null) {
            addresses.add(centralArrivalHub.getAddress());
        }

        addresses.add(arrivalHub.getAddress());
        return addresses;
    }
}
