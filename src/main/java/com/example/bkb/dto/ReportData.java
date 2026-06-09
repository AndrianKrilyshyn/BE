package com.example.bkb.dto;

import java.util.List;
import java.util.UUID;

public record ReportData(
        UUID attemptId,
        Integer trustScore,
        Integer recordingDurationSec,
        Integer totalViolations,
        List<ReportEvent> events
) {}
