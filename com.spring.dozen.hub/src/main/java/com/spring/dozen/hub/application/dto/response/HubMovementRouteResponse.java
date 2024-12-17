package com.spring.dozen.hub.application.dto.response;

import com.spring.dozen.hub.domain.entity.HubMovement;

import java.util.UUID;

public record HubMovementRouteResponse(
        UUID hubMovementId,
        UUID departureHubId,
        UUID arrivalHubId,
        int time,
        int distance
) {
    public static HubMovementRouteResponse from(HubMovement hubMovement) {
        return new HubMovementRouteResponse(
                hubMovement.getHubMovementId(),
                hubMovement.getDepartureHub().getHubId(),
                hubMovement.getArrivalHub().getHubId(),
                hubMovement.getTime(),
                hubMovement.getDistance()
        );
    }
}
