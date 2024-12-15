package com.spring.dozen.hub.application.dto.response;

import com.spring.dozen.hub.domain.entity.Hub;
import com.spring.dozen.hub.domain.entity.HubMovement;

import java.util.UUID;

public record HubMovementListResponse(
        UUID hubMovementId,
        UUID departureHubId,
        UUID arrivalHubId,
        boolean isDeleted
) {
    public static HubMovementListResponse from(HubMovement hubMovement) {
        return new HubMovementListResponse(
                hubMovement.getHubMovementId(),
                hubMovement.getDepartureHub().getHubId(),
                hubMovement.getArrivalHub().getHubId(),
                hubMovement.isDeleted()
        );
    }
}
