package com.spring.dozen.hub.presentation.dto;

import com.spring.dozen.hub.application.dto.HubDto;

import java.util.UUID;

public record HubRequestDto(
        Long userId,
        UUID centralHubId,
        String address
) {
    // Interface 계층과 Application 계층간 Object 분리 방법
    public HubDto toDTO() {
        return HubDto.of(
                this.userId,
                this.centralHubId,
                this.address
        );
    }
}
