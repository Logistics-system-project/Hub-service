package com.spring.dozen.hub.application.dto.response;

import com.spring.dozen.hub.domain.entity.Hub;

import java.util.UUID;

public record HubListResponse(
        UUID hubId,
        Long userId,
        String hubName,
        UUID centralHubId,
        String address,
        boolean isDeleted
) {
    public static HubListResponse from(Hub hub) {
        return new HubListResponse(
                hub.getHubId(),
                hub.getUserId(),
                hub.getHubName(),
                hub.getCentralHubId(),
                hub.getAddress(),
                hub.isDeleted()
        );
    }
}
