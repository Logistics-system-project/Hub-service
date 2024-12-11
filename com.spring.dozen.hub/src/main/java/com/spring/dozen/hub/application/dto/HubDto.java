package com.spring.dozen.hub.application.dto;

import java.util.UUID;

public record HubDto(
        Long userId,
        UUID centralHubId,
        String address,
        Double locationX,
        Double locationY
) {
    public static HubDto of(Long userId,
                            UUID centralHubId,
                            String address,
                            Double locationX,
                            Double locationY) {
        return new HubDto(userId, centralHubId, address, locationX, locationY);
    }
}
