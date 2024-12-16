package com.spring.dozen.hub.presentation.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record GeminiRequest(
        List<Content> contents,
        GenerationConfig generationConfig
) {
    public static GeminiRequest of(String query, List<String> addresses) {
        List<Part> partsList = new ArrayList<>();
        partsList.add(new Part(query));

        for (String address : addresses) {
            partsList.add(new Part("허브 주소: " + address));
        }

        Content content = new Content(partsList);
        return new GeminiRequest(Collections.singletonList(content), new GenerationConfig(1, 1000, 0.7));
    }

    public record Content(List<Part> parts) {}

    public record Part(String text) {}

    public record GenerationConfig(
            int candidate_count,
            int max_output_tokens,
            double temperature
    ) {}
}

