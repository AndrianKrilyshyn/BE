package com.example.bkb.dto;


import com.fasterxml.jackson.annotation.JsonRawValue;

import java.util.UUID;

public record TemplateResponse(
        UUID id,
        @JsonRawValue String contentSchema
) {}
