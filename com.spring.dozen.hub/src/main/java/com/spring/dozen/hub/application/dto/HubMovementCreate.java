package com.spring.dozen.hub.application.dto;

import java.util.UUID;

public record HubMovementCreate(
        UUID departureHubId,
        UUID arrivalHubId
) {
    public static HubMovementCreate of(UUID departureHubId,
                                       UUID arrivalHubId) {
        return new HubMovementCreate(departureHubId, arrivalHubId);
    }
}
