package com.spring.dozen.hub.presentation.dto;

import com.spring.dozen.hub.application.dto.HubDto;

import java.util.UUID;

public record HubRequest(
        Long userId,
        UUID centralHubId,
        String address
) {
    public HubDto toDTO() {
        return HubDto.of(
                this.userId,
                this.centralHubId,
                this.address
        );
    }
}
