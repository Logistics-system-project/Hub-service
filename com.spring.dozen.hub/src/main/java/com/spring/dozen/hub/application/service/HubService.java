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

    // 주문 접수
    @Transactional
    public HubResponseDto createHub(HubDto request){
        // Hub 엔티티에 객체 생성에 대한 책임을 부여
        Hub hub = Hub.create(
                request.userId(),
                request.centralHubId(),
                request.address(),
                request.locationX(),
                request.locationY()
        );
        hubRepository.save(hub);
        return HubResponseDto.from(hub);
    }
}
