package com.example.bkb.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StartAttemptRequest(
        @NotNull UUID testId
) {}
