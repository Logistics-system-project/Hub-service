package com.spring.dozen.hub.infrastructure.config;

import com.spring.dozen.hub.domain.entity.Hub;
import com.spring.dozen.hub.domain.repository.HubRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class InitialHubDataInserter {

    private final HubRepository hubRepository;

    @PostConstruct
    public void init() {

        List<Hub> initialHubs = List.of(
                new Hub(UUID.randomUUID(), "경기 남부 센터", 1L, null, "경기도 이천시 덕평로 257-21", 127.4735, 37.3485),
                new Hub(UUID.randomUUID(), "경기 북부 센터", 1L, null, "경기도 고양시 덕양구 권율대로 570", 126.8345, 37.6625),
                new Hub(UUID.randomUUID(), "서울특별시 센터", 1L, null, "서울특별시 송파구 송파대로 55", 127.1025, 37.5125),
                new Hub(UUID.randomUUID(), "인천광역시 센터", 1L, null, "인천 남동구 정각로 29", 126.6855, 37.4645),
                new Hub(UUID.randomUUID(), "강원특별자치도 센터", 1L, null, "강원특별자치도 춘천시 중앙로 1", 127.7355, 37.875),

                new Hub(UUID.randomUUID(), "대전광역시 센터", 1L, null, "대전 서구 둔산로 100", 127.4075, 36.3525),
                new Hub(UUID.randomUUID(), "충청남도 센터", 1L, null, "충남 홍성군 홍북읍 충남대로 21", 126.7445, 36.6785),
                new Hub(UUID.randomUUID(), "충청북도 센터", 1L, null, "충북 청주시 상당구 상당로 82", 127.495, 36.6285),
                new Hub(UUID.randomUUID(), "세종특별자치시 센터", 1L, null, "세종특별자치시 한누리대로 2130", 127.2895, 36.4805),
                new Hub(UUID.randomUUID(), "전북특별자치도 센터", 1L, null, "전북특별자치도 전주시 완산구 효자로 225", 127.1385, 35.835),
                new Hub(UUID.randomUUID(), "광주광역시 센터", 1L, null, "광주 서구 내방로 111", 126.8565, 35.1535),
                new Hub(UUID.randomUUID(), "전라남도 센터", 1L, null, "전남 무안군 삼향읍 오룡길 1", 126.6575, 34.8325),

                new Hub(UUID.randomUUID(), "대구광역시 센터", 1L, null, "대구 북구 태평로 161", 128.5925, 35.895),
                new Hub(UUID.randomUUID(), "경상북도 센터", 1L, null, "경북 안동시 풍천면 도청대로 455", 128.7315, 36.5545),
                new Hub(UUID.randomUUID(), "경상남도 센터", 1L, null, "경남 창원시 의창구 중앙대로 300", 128.6795, 35.2335),
                new Hub(UUID.randomUUID(), "부산광역시 센터", 1L, null, "부산 동구 중앙대로 206", 129.0325, 35.1155),
                new Hub(UUID.randomUUID(), "울산광역시 센터", 1L, null, "울산 남구 중앙로 201", 129.3245, 35.5455)
        );

        for (Hub hub : initialHubs) {
            if (!hubRepository.existsByHubName(hub.getHubName())) {
                hubRepository.save(hub);
            }
        }

        // 각 그룹에 따른 centralHubId 설정
        Map<String, List<String>> hubGroups = new HashMap<>();
        hubGroups.put("경기 남부", List.of("경기 북부", "서울특별시", "인천광역시", "강원특별자치도"));
        hubGroups.put("대전광역시", List.of("충청남도", "충청북도", "세종특별자치시", "전북특별자치도", "광주광역시", "전라남도"));
        hubGroups.put("대구광역시", List.of("경상북도", "경상남도", "부산광역시", "울산광역시"));

        for (Map.Entry<String, List<String>> entry : hubGroups.entrySet()) {
            Optional<Hub> centralHubOptional = hubRepository.findByHubName(entry.getKey() + " 센터");
            if (centralHubOptional.isPresent()) {
                Hub centralHub = centralHubOptional.get();
                for (String hubName : entry.getValue()) {
                    Optional<Hub> hubOptional  = hubRepository.findByHubName(hubName + " 센터");
                    if (hubOptional.isPresent()) {
                        Hub hub = hubOptional.get();
                        hub.setCentralHubId(centralHub.getHubId());
                        hubRepository.save(hub);
                    }
                }
            }
        }
    }
}
