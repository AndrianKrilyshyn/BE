package com.example.bkb.dto;

import tools.jackson.databind.JsonNode;

public record FinishAttemptRequest(
        JsonNode answersJson
) {}
