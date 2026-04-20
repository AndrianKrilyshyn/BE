package com.example.bkb.dto;

import java.time.Instant;
import java.util.UUID;

public record TestResponse(
        UUID id,
        UUID templateId,
        String title,
        UUID authorId,
        Instant startTime,
        Integer durationMinutes
) {}
