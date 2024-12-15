package com.spring.dozen.hub.presentation.dto;

import com.spring.dozen.hub.application.dto.HubMovementCreate;

import java.util.UUID;

public record HubMovementCreateRequest(
        UUID departureHubId,
        UUID arrivalHubId
) {
    public HubMovementCreate toDTO() {
        return HubMovementCreate.of(
                this.departureHubId,
                this.arrivalHubId
        );
    }
}
