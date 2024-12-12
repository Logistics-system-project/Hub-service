package com.spring.dozen.hub.application.service;

import com.spring.dozen.hub.application.dto.HubDto;
import com.spring.dozen.hub.application.dto.response.HubDetailResponse;
import com.spring.dozen.hub.application.dto.response.HubListResponse;
import com.spring.dozen.hub.application.dto.response.HubResponse;
import com.spring.dozen.hub.application.exception.ErrorCode;
import com.spring.dozen.hub.application.exception.HubException;
import com.spring.dozen.hub.domain.entity.Hub;
import com.spring.dozen.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;
    private final AddressToCoordinateService addressToCoordinateService;

    // 허브 생성
    @Transactional
    public HubResponse createHub(HubDto request){
        // 유저 확인

        // 주소로 위도, 경도 찾기
        double[] coordinates = addressToCoordinateService.getCoordinates(request.address());

        Double locationX = coordinates[1];
        Double locationY = coordinates[0];

        // Hub 엔티티에 객체 생성에 대한 책임을 부여
        Hub hub = Hub.create(
                request.userId(),
                request.centralHubId(),
                request.address(),
                locationX,
                locationY
        );
        hubRepository.save(hub);

        return HubResponse.from(hub);
    }

    // 허브 목록 조회
    @Transactional
    public Page<HubListResponse> getHubList(int page, int size, String sortBy, boolean isAsc, String keyword){
        size = (size == 10 || size == 30 || size == 50) ? size : 10;
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy.equals("updated_at") ? "updated_at" : "created_at");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Hub> hubPage = hubRepository.findByKeyword(keyword, pageable);

        return hubPage.map(HubListResponse::from);
    }

    // 허브 상세 조회
    @Transactional
    public HubDetailResponse getHubDetail(UUID hubId){
        Hub hub = hubRepository.findByHubIdAndIsDeletedFalse(hubId)
                .orElseThrow(() -> new HubException(ErrorCode.NOT_FOUND_HUB));
        return HubDetailResponse.from(hub);
    }

    // 허브 수정
    @Transactional
    public HubDetailResponse updateHub(UUID hubId,HubDto request){
        // 해당 허브
        Hub hub = hubRepository.findByHubIdAndIsDeletedFalse(hubId)
                .orElseThrow(() -> new HubException(ErrorCode.NOT_FOUND_HUB));

        // 유저값 확인
        if (request.userId() != null) {
            // 유저 유효성 검증

            hub.setUserId(request.userId());
        }

        // 중앙허브값 확인
        if (request.centralHubId() != null) {
            // 중앙 허브 유효성 검증
            hubRepository.findByHubIdAndIsDeletedFalse(request.centralHubId())
                    .orElseThrow(() -> new HubException(ErrorCode.NOT_FOUND_HUB));
            hub.setCentralHubId(request.centralHubId());
        }

        // 주소값
        if (request.address() != null && !request.address().isEmpty()) {
            double[] coordinates = addressToCoordinateService.getCoordinates(request.address());
            hub.setAddress(request.address());
            hub.setLocationX(coordinates[1]); // 경도
            hub.setLocationY(coordinates[0]); // 위도
        }

        return HubDetailResponse.from(hub);
    }

    // 허브 삭제
    @Transactional
    public void deleteHub(UUID hubId) {
        // 해당 허브
        Hub hub = hubRepository.findByHubIdAndIsDeletedFalse(hubId)
                .orElseThrow(() -> new HubException(ErrorCode.NOT_FOUND_HUB));

        hub.deleteHud(true);

        hub.setDeletedAt(LocalDateTime.now());
        //hub.setDeletedBy("SystemAdmin");
    }


}
