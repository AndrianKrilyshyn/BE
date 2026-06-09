package com.example.bkb.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;

import java.time.Instant;
import java.util.UUID;

public record StartAttemptResponse(
        UUID attemptId,
        UUID testId,
        String title,
        Integer durationMinutes,
        @JsonRawValue String contentSchema,
        Instant startedAt
) {}
