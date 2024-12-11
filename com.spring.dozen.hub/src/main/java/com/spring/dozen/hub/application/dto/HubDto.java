package com.spring.dozen.hub.application.dto;

import java.util.UUID;

public record HubDto(
        Long userId,
        UUID centralHubId,
        String address
) {
    public static HubDto of(Long userId,
                            UUID centralHubId,
                            String address) {
        return new HubDto(userId, centralHubId, address);
    }
}
