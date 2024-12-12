package com.spring.dozen.hub.application.dto.response;

import com.spring.dozen.hub.domain.entity.Hub;

import java.time.LocalDateTime;
import java.util.UUID;

public record HubDetailResponseDto(
        UUID hubId,
        Long userId,
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
    public static HubDetailResponseDto from(Hub hub) {
        return new HubDetailResponseDto(
                hub.getHubId(),
                hub.getUserId(),
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
