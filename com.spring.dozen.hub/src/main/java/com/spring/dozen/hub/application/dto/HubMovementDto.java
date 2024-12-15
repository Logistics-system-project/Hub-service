package com.spring.dozen.hub.application.dto;

import java.util.UUID;

public record HubMovementDto(
        UUID departureHubId,
        UUID arrivalHubId
) {
    public static HubMovementDto of(UUID departureHubId,
                                    UUID arrivalHubID) {
        return new HubMovementDto(departureHubId, arrivalHubID);
    }
}
