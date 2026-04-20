package com.example.bkb.controller;

import com.example.bkb.dto.CreateTemplateRequest;
import com.example.bkb.dto.TemplateResponse;
import com.example.bkb.service.TestTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tests/templates")
@RequiredArgsConstructor
public class TestTemplateController {

    private final TestTemplateService templateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('TEACHER')")
    public TemplateResponse create(@Valid @RequestBody CreateTemplateRequest req) {
        return templateService.create(req);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public TemplateResponse getById(@PathVariable UUID id) {
        return templateService.getById(id);
    }
}
