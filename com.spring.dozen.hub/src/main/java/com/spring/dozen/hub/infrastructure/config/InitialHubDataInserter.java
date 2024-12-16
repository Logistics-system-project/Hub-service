package com.spring.dozen.hub.infrastructure.config;

import com.spring.dozen.hub.domain.entity.Hub;
import com.spring.dozen.hub.domain.repository.HubRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class InitialHubDataInserter {

    private final HubRepository hubRepository;

    @PostConstruct
    public void init() {
        UUID GyeonggiSouthernId = UUID.randomUUID();
        UUID DaejeonId = UUID.randomUUID();
        UUID DaeguId = UUID.randomUUID();

        List<Hub> initialHubs = List.of(
                new Hub(GyeonggiSouthernId, "경기 남부 센터", 1L, null, "경기도 이천시 덕평로 257-21", 37.3485, 127.4735),
                new Hub(UUID.randomUUID(), "경기 북부 센터", 1L, GyeonggiSouthernId, "경기도 고양시 덕양구 권율대로 570", 37.6625, 126.8345),
                new Hub(UUID.randomUUID(), "서울특별시 센터", 1L, GyeonggiSouthernId, "서울특별시 송파구 송파대로 55", 37.5125, 127.1025),
                new Hub(UUID.randomUUID(), "인천광역시 센터", 1L, GyeonggiSouthernId, "인천 남동구 정각로 29", 37.4645, 126.6855),
                new Hub(UUID.randomUUID(), "강원특별자치도 센터", 1L, GyeonggiSouthernId, "강원특별자치도 춘천시 중앙로 1", 37.875, 127.7355),

                new Hub(DaejeonId, "대전광역시 센터", 1L, null, "대전 서구 둔산로 100", 36.3525, 127.4075),
                new Hub(UUID.randomUUID(), "충청남도 센터", 1L, DaejeonId, "충남 홍성군 홍북읍 충남대로 21", 36.6785, 126.7445),
                new Hub(UUID.randomUUID(), "충청북도 센터", 1L, DaejeonId, "충북 청주시 상당구 상당로 82", 36.6285, 127.495),
                new Hub(UUID.randomUUID(), "세종특별자치시 센터", 1L, DaejeonId, "세종특별자치시 한누리대로 2130", 36.4805, 127.2895),
                new Hub(UUID.randomUUID(), "전북특별자치도 센터", 1L, DaejeonId, "전북특별자치도 전주시 완산구 효자로 225", 35.835, 127.1385),
                new Hub(UUID.randomUUID(), "광주광역시 센터", 1L, DaejeonId, "광주 서구 내방로 111", 35.1535, 126.8565),
                new Hub(UUID.randomUUID(), "전라남도 센터", 1L, DaejeonId, "전남 무안군 삼향읍 오룡길 1", 34.8325, 126.6575),

                new Hub(DaeguId, "대구광역시 센터", 1L, null, "대구 북구 태평로 161", 35.895, 128.5925),
                new Hub(UUID.randomUUID(), "경상북도 센터", 1L, DaeguId, "경북 안동시 풍천면 도청대로 455", 36.5545, 128.7315),
                new Hub(UUID.randomUUID(), "경상남도 센터", 1L, DaeguId, "경남 창원시 의창구 중앙대로 300", 35.2335, 128.6795),
                new Hub(UUID.randomUUID(), "부산광역시 센터", 1L, DaeguId, "부산 동구 중앙대로 206", 35.1155, 129.0325),
                new Hub(UUID.randomUUID(), "울산광역시 센터", 1L, DaeguId, "울산 남구 중앙로 201", 35.5455, 129.3245)
        );

        for (Hub hub : initialHubs) {
            if (!hubRepository.existsByHubName(hub.getHubName())) {
                hubRepository.save(hub);
            }
        }
    }
}
