package com.spring.dozen.hub.presentation.dto;

import com.spring.dozen.hub.application.dto.HubMovementUpdate;

public record HubMovementUpdateRequest(
        int time,
        int distance

) {
    public HubMovementUpdate toDTO() {
        return HubMovementUpdate.of(
                this.time,
                this.distance
        );
    }
}
