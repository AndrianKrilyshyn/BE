package com.example.bkb.dto;

import com.example.bkb.domain.enums.AttemptStatus;
import com.fasterxml.jackson.annotation.JsonRawValue;

import java.time.Instant;
import java.util.UUID;

public record AttemptDetailResponse(
        UUID attemptId,
        String candidateEmail,
        String candidateName,
        AttemptStatus status,
        Instant startedAt,
        @JsonRawValue String answersJson,
        @JsonRawValue String contentSchema,
        ReportResponse report
) {}
