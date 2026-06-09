package com.example.bkb.dto;

import tools.jackson.databind.JsonNode;

public record UpdateTemplateRequest(
        String title,
        JsonNode contentSchema
) {}
