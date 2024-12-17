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
import com.spring.dozen.hub.presentation.dto.KakaoApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubMovementService {

    private final HubRepository hubRepository;
    private final HubMovementRepository hubMovementRepository;

    private final RestTemplate restTemplate;

    @Value("${kakao.api.url}")
    private String kakaoApiUrl;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    // 허브 이동정보 생성
    @Transactional
    public HubMovementResponse createHubMovement(HubMovementCreate request) {
        // 출발 허브
        Hub departureHub = findValidHub(request.departureHubId());
        // 도착 허브
        Hub arrivalHub = findValidHub(request.arrivalHubId());

        // 기존 전체 허브 이동 정보 조회
        HubMovement existingOverallMovement = findExistingHubMovement(departureHub, arrivalHub);

        if (existingOverallMovement != null) {
            // 이미 전체 허브 이동 정보가 존재하는 경우
            throw new HubException(ErrorCode.ALREADY_EXISTS_HUBMOVEMENT);
        }

        // 출발 허브의 중앙 허브
        Hub centralDepartureHub = findCentralHub(departureHub);

        // 도착 허브의 중앙 허브 정보
        Hub centralArrivalHub = findCentralHub(arrivalHub);

        // 이동 경로 허브 리스트
        List<Hub> hubRoute = buildRoute(departureHub, centralDepartureHub, centralArrivalHub, arrivalHub);

        int totalDistance = 0; // 전체 경로의 총 거리 (m)
        int totalDuration = 0; // 전체 경로의 총 소요 시간 (초)

        // 각 허브 간 이동 정보를 처리
        for (int i = 0; i < hubRoute.size() - 1; i++) {
            Hub originHub = hubRoute.get(i);
            Hub destinationHub = hubRoute.get(i + 1);

            // 기존 허브 이동 정보 조회
            HubMovement existingMovement = findExistingHubMovement(originHub, destinationHub);

            int segmentDistance;
            int segmentDuration;

            if (existingMovement != null) {
                // 기존 데이터 활용
                segmentDistance = existingMovement.getDistance();
                segmentDuration = existingMovement.getTime();
            } else {
                // 이동 정보 API 호출
                KakaoApiResponse response = getDirections(originHub, destinationHub);

                if (response != null && Arrays.asList(response.routes()).size() > 0) {
                    KakaoApiResponse.Route route = Arrays.asList(response.routes()).get(0);
                    segmentDistance = route.summary().distance();
                    segmentDuration = route.summary().duration();

                    // 새로운 이동 정보 저장
                    HubMovement newMovement = HubMovement.create(
                            originHub,
                            destinationHub,
                            segmentDuration,
                            segmentDistance
                    );
                    hubMovementRepository.save(newMovement);
                } else {
                    throw new HubException(ErrorCode.FAILED_TO_RETRIEVE_ROUTE);
                }
            }

            // 전체 경로의 거리 및 시간 업데이트
            totalDistance += segmentDistance;
            totalDuration += segmentDuration;
        }

        // 전체 경로에 대한 HubMovement 생성 및 저장
        HubMovement overallMovement = HubMovement.create(
                departureHub,
                arrivalHub,
                totalDuration,
                totalDistance
        );
        hubMovementRepository.save(overallMovement);

        return HubMovementResponse.from(overallMovement);
    }

    // 허브 이동정보 목록 조회
//    @Cacheable(cacheNames = "hubMovementListCache",
//            key = "{ args[0], args[1].pageNumber, args[2].pageSize }")
    @Transactional
    public Page<HubMovementListResponse> getHubMovementList(String keyword, Pageable pageable){
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
    @Cacheable(cacheNames = "hubMovementCache", key = "args[0]")
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

    // 허브 이동정보 삭제
    @Transactional
    public void deleteHubMovement(UUID hubMovementId, String userId) {
        // 해당 허브
        HubMovement hubMovement = hubMovementRepository.findByHubMovementIdAndIsDeletedFalse(hubMovementId)
                .orElseThrow(() -> new HubException(ErrorCode.NOT_FOUND_HUBMOVEMENT));

        hubMovement.delete(userId);
    }

    // 허브 이동경로 조회
    @Transactional
    public List<HubMovementRouteResponse> getHubRoute(UUID departureHubId, UUID arrivalHubId) {
        // 출발 허브
        Hub departureHub = findValidHub(departureHubId);
        // 도착 허브
        Hub arrivalHub = findValidHub(arrivalHubId);

        // 이동 경로 허브 리스트
        Set<UUID> hubRoute = new LinkedHashSet<>();
        hubRoute.add(departureHubId);

        if (departureHub.getCentralHubId() != null) {
            hubRoute.add(departureHub.getCentralHubId());
        }

        if (arrivalHub.getCentralHubId() != null) {
            hubRoute.add(arrivalHub.getCentralHubId());
        }

        hubRoute.add(arrivalHubId);

        // 허브 ID 리스트를 순서대로 처리
        List<UUID> hubIdList = new ArrayList<>(hubRoute);

        List<HubMovementRouteResponse> RouteResponse = new ArrayList<>();

        // 모든 허브 이동 정보 조회 및 반복 처리
        for (int i = 0; i < hubIdList.size() - 1; i++) {
            UUID currentHubId = hubIdList.get(i);
            UUID nextHubId = hubIdList.get(i + 1);

            HubMovement hubMovement = findHubMovement(currentHubId, nextHubId);
            RouteResponse.add(HubMovementRouteResponse.from(hubMovement));
        }
        return RouteResponse;
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

    // 허브 이동 정보를 조회하는 메서드
    private HubMovement findHubMovement(UUID departureHubId, UUID arrivalHubId) {
        return hubMovementRepository.findByDepartureHub_HubIdAndArrivalHub_HubId(departureHubId, arrivalHubId)
                .orElseThrow(() -> new HubException(ErrorCode.NOT_FOUND_HUBMOVEMENT));
    }

    private HubMovement findExistingHubMovement(Hub departureHubId, Hub arrivalHub) {
        return hubMovementRepository.findByDepartureHub_HubIdAndArrivalHub_HubId(departureHubId.getHubId(), arrivalHub.getHubId()).orElse(null);
    }

    private List<Hub> buildRoute(Hub departureHub, Hub centralDepartureHub, Hub centralArrivalHub, Hub arrivalHub) {
        Set<Hub> hubRoute = new LinkedHashSet<>();
        hubRoute.add(departureHub);

        if (centralDepartureHub != null) {
            hubRoute.add(centralDepartureHub);
        }

        if (centralArrivalHub != null) {
            hubRoute.add(centralArrivalHub);
        }

        hubRoute.add(arrivalHub);
        return new ArrayList<>(hubRoute);
    }

    public KakaoApiResponse getDirections(Hub originHub, Hub destinationHub) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        String url = UriComponentsBuilder.fromHttpUrl(kakaoApiUrl)
                .queryParam("origin", originHub.getLocationX() + "," + originHub.getLocationY())
                .queryParam("destination", destinationHub.getLocationX() + "," + destinationHub.getLocationY())
                .queryParam("priority", "RECOMMEND")
                .toUriString();

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<KakaoApiResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                KakaoApiResponse.class
        );

        return response.getBody();
    }
}
