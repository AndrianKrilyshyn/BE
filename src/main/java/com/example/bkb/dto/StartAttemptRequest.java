package com.example.bkb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record StartAttemptRequest(
        @NotBlank String accessCode,
        @NotBlank @Email String candidateEmail,
        @NotBlank String candidateName
) {}
