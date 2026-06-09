package com.example.bkb.dto;

import com.example.bkb.domain.enums.Severity;

import java.util.List;

public record ReportEvent(
        Integer startTimeSec,
        Integer endTimeSec,
        Severity severity,
        Double confidence,
        String clipKey,
        String thumbnailKey,
        List<Violation> violations
) {}
