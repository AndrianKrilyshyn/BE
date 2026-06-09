package com.example.bkb.dto;

import com.example.bkb.domain.enums.AttemptStatus;

import java.util.UUID;

public record AttemptSummaryResponse(
        UUID attemptId,
        UUID candidateId,
        String candidateEmail,
        String candidateName,
        String testName,
        Integer score,
        Integer trustScore,
        AttemptStatus status
) {}
