package com.example.bkb.dto;

import com.example.bkb.domain.enums.AttemptStatus;

import java.time.Instant;
import java.util.UUID;

public record AttemptResponse(
        UUID id,
        AttemptStatus status,
        Instant startedAt
) {}
