package com.example.bkb.dto;


import com.fasterxml.jackson.annotation.JsonRawValue;

import java.util.UUID;

public record ReportResponse(
        UUID id,
        UUID attemptId,
        Integer trustScore,
        Integer totalViolations,
        @JsonRawValue String reportMetadata
) {}
