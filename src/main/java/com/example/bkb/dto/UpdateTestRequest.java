package com.example.bkb.dto;

import com.example.bkb.domain.enums.TestStatus;
import jakarta.validation.constraints.Min;

public record UpdateTestRequest(
        String title,
        @Min(1) Integer durationMinutes,
        TestStatus status
) {}
