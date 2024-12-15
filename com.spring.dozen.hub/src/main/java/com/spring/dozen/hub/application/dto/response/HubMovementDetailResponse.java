package com.spring.dozen.hub.application.dto.response;

import com.spring.dozen.hub.domain.entity.HubMovement;

import java.time.LocalDateTime;
import java.util.UUID;

public record HubMovementDetailResponse(
        UUID hubMovementId,
        UUID departureHubId,
        UUID arrivalHubId,
        String formattedTime,
        String getFormattedDistance,
        boolean isDeleted,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy

) {
    public static HubMovementDetailResponse from(HubMovement hubMovement) {
        return new HubMovementDetailResponse(
                hubMovement.getHubMovementId(),
                hubMovement.getDepartureHub().getHubId(),
                hubMovement.getArrivalHub().getHubId(),
                hubMovement.getFormattedTime(),
                hubMovement.getFormattedDistance(),
                hubMovement.isDeleted(),
                hubMovement.getCreatedAt(),
                hubMovement.getCreatedBy(),
                hubMovement.getUpdatedAt(),
                hubMovement.getUpdatedBy()
        );
    }
}
