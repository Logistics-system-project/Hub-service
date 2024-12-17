package com.spring.dozen.hub.application.dto;

import java.util.UUID;

public record HubDto(
        Long userId,
        String hubName,
        UUID centralHubId,
        String address
) {
    public static HubDto of(Long userId,
                            String hubName,
                            UUID centralHubId,
                            String address) {
        return new HubDto(userId, hubName, centralHubId, address);
    }
}
