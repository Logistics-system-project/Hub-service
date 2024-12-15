package com.spring.dozen.hub.presentation.dto;

import com.spring.dozen.hub.application.dto.HubMovementDto;

import java.util.UUID;

public record Request(
        String query,
        String departureHubAddress,
        String centralDepartureHubAddress,
        String centralArrivalHubAddress,
        String arrivalHubAddress
) {

}