package com.spring.dozen.hub.application.dto;

public record HubMovementUpdate(
       int time,
        int distance
) {
    public static HubMovementUpdate of(int time,
                                       int distance) {
        return new HubMovementUpdate(time, distance);
    }
}
