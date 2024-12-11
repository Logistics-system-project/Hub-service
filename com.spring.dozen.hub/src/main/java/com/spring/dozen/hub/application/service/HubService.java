package com.spring.dozen.hub.application.service;

import com.spring.dozen.hub.application.dto.HubDto;
import com.spring.dozen.hub.application.dto.response.HubResponseDto;
import com.spring.dozen.hub.domain.entity.Hub;
import com.spring.dozen.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;
    private final AddressToCoordinateService addressToCoordinateService;

    // 주문 접수
    @Transactional
    public HubResponseDto createHub(HubDto request){

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

        return HubResponseDto.from(hub);
    }
}
