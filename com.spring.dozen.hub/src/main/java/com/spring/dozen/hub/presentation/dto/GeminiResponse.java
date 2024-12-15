package com.spring.dozen.hub.presentation.dto;

import java.util.List;

public record GeminiResponse(
        List<Candidate> candidates
) {
    public record Candidate(
            Content content,
            String finishReason,
            int index,
            List<SafetyRating> safetyRatings
    ) {}

    public record Content(
            List<TextPart> parts,
            String role
    ) {}

    public record TextPart(
            String text
    ) {}

    public record SafetyRating(
            String category,
            String probability
    ) {}
}

