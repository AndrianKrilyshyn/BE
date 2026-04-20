package com.example.bkb.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record CreateTestRequest(
        @NotNull UUID templateId,
        @NotBlank String title,
        @NotNull Instant startTime,
        @NotNull @Min(1) Integer durationMinutes
) {}
