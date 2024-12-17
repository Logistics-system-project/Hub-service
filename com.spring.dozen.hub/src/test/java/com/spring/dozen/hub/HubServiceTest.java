package com.spring.dozen.hub;

import com.spring.dozen.hub.application.dto.HubDto;
import com.spring.dozen.hub.application.dto.response.HubResponse;
import com.spring.dozen.hub.application.service.AddressToCoordinateService;
import com.spring.dozen.hub.application.service.HubService;
import com.spring.dozen.hub.domain.entity.Hub;
import com.spring.dozen.hub.domain.repository.HubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HubServiceTest {

    @Mock
    private HubRepository hubRepository;

    @Mock
    private AddressToCoordinateService addressToCoordinateService;

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
        String hubName = "테스트";
        UUID centralHubId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        String address = "전북 익산시 망산길 11-17";
        Double locationY = 35.976749396987046;
        Double locationX = 126.99599512792346;

        HubDto request = new HubDto(userId,  hubName,centralHubId, address);

        double[] mockCoordinates = {locationY, locationX};

        when(addressToCoordinateService .getCoordinates(address)).thenReturn(mockCoordinates);

        Hub mockHub = Hub.create(userId, hubName, centralHubId, address, locationY, locationX);
        when(hubRepository.save(any(Hub.class))).thenReturn(mockHub);

        // when
        HubResponse response = hubService.createHub(request);

        // then
        assertNotNull(response);
        assertEquals(userId, response.userId());
        assertEquals(centralHubId, response.centralHubId());
        assertEquals(address, response.address());
        assertEquals(locationX, response.locationX());
        assertEquals(locationY, response.locationY());

        // 저장된 Hub 엔티티 값 검증
        ArgumentCaptor<Hub> captor = ArgumentCaptor.forClass(Hub.class);
        verify(hubRepository).save(captor.capture());
        Hub capturedHub = captor.getValue();

        assertEquals(userId, capturedHub.getUserId());
        assertEquals(centralHubId, capturedHub.getCentralHubId());
        assertEquals(address, capturedHub.getAddress());
        assertEquals(locationX, capturedHub.getLocationX());
        assertEquals(locationY, capturedHub.getLocationY());
    }
}
