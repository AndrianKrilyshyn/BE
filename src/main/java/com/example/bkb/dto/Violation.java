package com.example.bkb.dto;

import com.example.bkb.domain.enums.Severity;
import com.example.bkb.domain.enums.ViolationCategory;

public record Violation(
        ViolationCategory category,
        Integer startTimeSec,
        Integer endTimeSec,
        Severity severity,
        Double confidence
) {}
