package com.example.bkb.dto;

import tools.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;

public record CreateTemplateRequest(
        @NotNull JsonNode contentSchema
) {}
