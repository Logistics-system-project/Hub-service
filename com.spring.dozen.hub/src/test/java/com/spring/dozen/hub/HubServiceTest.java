package com.spring.dozen.hub;

import com.spring.dozen.hub.application.dto.HubDto;
import com.spring.dozen.hub.application.dto.response.HubResponseDto;
import com.spring.dozen.hub.application.service.HubService;
import com.spring.dozen.hub.domain.entity.Hub;
import com.spring.dozen.hub.domain.repository.HubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HubServiceTest {

    @Mock
    private HubRepository hubRepository;

    @InjectMocks
    private HubService hubService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 초기화
    }

    @Test
    @DisplayName("허브 생성 테스트")
    void testCreateHub() {
        // given
        Long userId = 1L;
        UUID centralHubId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        String address = "123대로";
        Double locationX = 37.5665;
        Double locationY = 126.9780;

        HubDto request = new HubDto(userId, centralHubId, address, locationX, locationY);

        // Mock Hub 생성
        Hub hub = Hub.create(userId, centralHubId, address, locationX, locationY);
        when(hubRepository.save(any(Hub.class))).thenReturn(hub); // save 메서드 Mock

        // when
        HubResponseDto response = hubService.createHub(request);

        // then
        assertNotNull(response);
        assertEquals(userId, response.userId());
        assertEquals(centralHubId, response.centralHubId());
        assertEquals(address, response.address());
        assertEquals(locationX, response.locationX());
        assertEquals(locationY, response.locationY());
    }
}
