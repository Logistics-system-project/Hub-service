package com.spring.dozen.hub.application.service;

import com.spring.dozen.hub.application.dto.HubMovementDto;
import com.spring.dozen.hub.application.dto.response.*;
import com.spring.dozen.hub.application.exception.ErrorCode;
import com.spring.dozen.hub.application.exception.HubException;
import com.spring.dozen.hub.domain.entity.Hub;
import com.spring.dozen.hub.domain.entity.HubMovement;
import com.spring.dozen.hub.domain.repository.HubMovementRepository;
import com.spring.dozen.hub.domain.repository.HubRepository;
import com.spring.dozen.hub.presentation.dto.GeminiRequest;
import com.spring.dozen.hub.presentation.dto.GeminiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubMovementService {

    private final HubRepository hubRepository;
    private final HubMovementRepository hubMovementRepository;

    @Qualifier("geminiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    // 허브 이동정보 생성
    @Transactional
    public HubMovementResponse createHubMovement(HubMovementDto request){
        UUID departureHubId = request.departureHubId();
        UUID arrivalHubId = request.arrivalHubId();

        Hub departureHub = hubRepository.findByHubIdAndIsDeletedFalse(departureHubId)
                .orElseThrow(() -> new HubException(ErrorCode.NOT_FOUND_HUB));

        Hub arrivalHub = hubRepository.findByHubIdAndIsDeletedFalse(arrivalHubId)
                .orElseThrow(() -> new HubException(ErrorCode.NOT_FOUND_HUB));

        // 출발 허브의 중앙 허브
        Hub centralDepartureHub = null;
        if (departureHub.getCentralHubId() != null) {
            centralDepartureHub = hubRepository.findByHubIdAndIsDeletedFalse(departureHub.getCentralHubId())
                    .orElseThrow(() -> new HubException(ErrorCode.NOT_FOUND_HUB));
        }

        // 도착 허브의 중앙 허브 정보
        Hub centralArrivalHub = null;
        if (arrivalHub.getCentralHubId() != null) {
            centralArrivalHub = hubRepository.findByHubIdAndIsDeletedFalse(arrivalHub.getCentralHubId())
                    .orElseThrow(() -> new HubException(ErrorCode.NOT_FOUND_HUB));
        }

        // 이동 경로 설정
        List<String> addresses = new ArrayList<>();

        addresses.add(departureHub.getAddress());

        if (centralDepartureHub != null) {
            addresses.add(centralDepartureHub.getAddress());
        }

        if (centralArrivalHub != null) {
            addresses.add(centralArrivalHub.getAddress());
        }

        addresses.add(arrivalHub.getAddress());

        String query = "허브를 순서대로 이동할 때의 총 소요시간과 총 이동거리를 계산해주세요.";

        // Gemini 요청 생성
        GeminiRequest geminiRequest = GeminiRequest.of(query, addresses);

        // Gemini API 호출
         String response = findHubMovement(geminiRequest);

        // Gemini 응답에서 거리 및 시간 추출
        String timePattern = "총 소요시간:\\s*약\\s*(\\d+)시간\\s*(\\d+)분";
        String distancePattern = "총 이동거리:\\s*약\\s*(\\d+)km";

        Pattern timeRegex = Pattern.compile(timePattern);
        Matcher timeMatcher = timeRegex.matcher(response);

        Pattern distanceRegex = Pattern.compile(distancePattern);
        Matcher distanceMatcher = distanceRegex.matcher(response);

        int hours = 0;
        int minutes = 0;
        int totalDistance = 0;

        if (timeMatcher.find()) {
            hours = Integer.parseInt(timeMatcher.group(1)); // 시간 추출
            minutes = Integer.parseInt(timeMatcher.group(2)); // 분 추출
        }

        if (distanceMatcher.find()) {
            totalDistance += Integer.parseInt(distanceMatcher.group(1)); // 이동 거리 추출
        }

        int totalTime = hours * 60 + minutes; // 분 단위로 변환

        HubMovement hubMovement = HubMovement.create(
                departureHub,
                arrivalHub,
                totalTime,
                totalDistance
        );
        hubMovementRepository.save(hubMovement);

        return HubMovementResponse.from(hubMovement);
    }

    // Gemini : 허브 이동 경로 탐색
    public String findHubMovement (GeminiRequest request) {

        // Gemini에 요청 전송
        String requestUrl = apiUrl + "?key=" + apiKey;

        GeminiResponse response = restTemplate.postForObject(requestUrl, request, GeminiResponse.class);

        log.info("GeminiResponse: {}", response);

        return response.candidates().get(0).content().parts().get(0).text();
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
}
