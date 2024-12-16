package com.spring.dozen.hub.application.dto.response;

import com.spring.dozen.hub.domain.entity.HubMovement;

import java.util.UUID;

public record HubMovementResponse(
        UUID hubMovementId,
        UUID departureHubId,
        UUID arrivalHubId,
        String formattedTime,
        String getFormattedDistance
) {
    public static HubMovementResponse from(HubMovement hubMovement) {
        return new HubMovementResponse(
                hubMovement.getHubMovementId(),
                hubMovement.getDepartureHub().getHubId(),
                hubMovement.getArrivalHub().getHubId(),
                hubMovement.getFormattedTime(),
                hubMovement.getFormattedDistance()
        );
    }
}
