package com.example.bkb.dto;

import java.util.List;
import java.util.UUID;

public record ReportResponse(
        UUID id,
        UUID attemptId,
        Integer trustScore,
        Integer totalViolations,
        Integer recordingDurationSec,
        List<EventResponse> events
) {}
