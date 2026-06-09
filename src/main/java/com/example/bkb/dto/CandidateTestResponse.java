package com.example.bkb.dto;

import com.example.bkb.domain.enums.TestStatus;

import java.util.UUID;

public record CandidateTestResponse(
        UUID testId,
        String title,
        Integer durationMinutes,
        TestStatus status
) {}
