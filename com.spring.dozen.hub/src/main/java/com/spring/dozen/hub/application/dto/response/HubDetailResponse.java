package com.spring.dozen.hub.application.dto.response;

import com.spring.dozen.hub.domain.entity.Hub;

import java.time.LocalDateTime;
import java.util.UUID;

public record HubDetailResponse(
        UUID hubId,
        Long userId,
        String hubName,
        UUID centralHubId,
        String address,
        Double locationX,
        Double locationY,
        boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy

) {
    public static HubDetailResponse from(Hub hub) {
        return new HubDetailResponse(
                hub.getHubId(),
                hub.getUserId(),
                hub.getHubName(),
                hub.getCentralHubId(),
                hub.getAddress(),
                hub.getLocationX(),
                hub.getLocationY(),
                hub.isDeleted(),
                hub.getCreatedAt(),
                hub.getCreatedBy(),
                hub.getUpdatedAt(),
                hub.getUpdatedBy()
        );
    }
}
