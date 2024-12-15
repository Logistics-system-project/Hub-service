package com.spring.dozen.hub.presentation.dto;

import com.spring.dozen.hub.application.dto.HubMovementDto;

import java.util.UUID;

public record HubMovementRequest(
        UUID departureHubId,
        UUID arrivalHubId
) {
    public HubMovementDto toDTO() {
        return HubMovementDto.of(
                this.departureHubId,
                this.arrivalHubId
        );
    }
}
