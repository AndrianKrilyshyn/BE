package com.example.bkb.dto;

import com.example.bkb.domain.enums.TestStatus;

import java.time.Instant;
import java.util.UUID;

public record TestResponse(
        UUID id,
        UUID templateId,
        String title,
        String accessCode,
        TestStatus status,
        UUID authorId,
        Integer durationMinutes
) {}
