package com.example.bkb.dto;

import com.example.bkb.domain.enums.Severity;

import java.util.List;

public record EventResponse(
        Integer startTimeSec,
        Integer endTimeSec,
        Severity severity,
        Double confidence,
        String clipUrl,
        String thumbnailUrl,
        List<ViolationResponse> violations
) {}
