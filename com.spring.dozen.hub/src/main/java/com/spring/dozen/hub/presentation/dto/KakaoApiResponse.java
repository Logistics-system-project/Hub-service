package com.spring.dozen.hub.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoApiResponse (
        Route[] routes
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Route(
            Summary summary
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Summary(
            int distance,
            int duration
    ) {
    }
}
