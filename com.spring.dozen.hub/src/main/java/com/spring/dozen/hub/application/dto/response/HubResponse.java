package com.spring.dozen.hub.application.dto.response;

import com.spring.dozen.hub.domain.entity.Hub;

import java.util.UUID;

public record HubResponse(
        UUID hubId,
        Long userId,
        UUID centralHubId,
        String address,
        Double locationX,
        Double locationY
) {
    public static HubResponse from(Hub hub) {
        return new HubResponse(
                hub.getHubId(),
                hub.getUserId(),
                hub.getCentralHubId(),
                hub.getAddress(),
                hub.getLocationX(),
                hub.getLocationY()
        );
    }
}
