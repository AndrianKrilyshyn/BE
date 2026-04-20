package com.example.bkb.dto;

import java.util.UUID;

public record ProctoringResultMessage(
        UUID attemptId,
        Integer trustScore,
        Integer totalViolations,
        String reportMetadata
) {}
