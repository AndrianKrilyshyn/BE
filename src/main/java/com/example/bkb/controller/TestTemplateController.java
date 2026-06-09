package com.example.bkb.controller;

import com.example.bkb.dto.*;
import com.example.bkb.service.TestTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tests/templates")
@RequiredArgsConstructor
public class TestTemplateController {

    private final TestTemplateService templateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('EXAMINER')")
    public TemplateResponse create(@Valid @RequestBody CreateTemplateRequest req, Authentication auth) {
        return templateService.create(req, (UUID) auth.getPrincipal());
    }

    @GetMapping
    @PreAuthorize("hasRole('EXAMINER')")
    public List<TemplateResponse> getMyTemplates(Authentication auth) {
        return templateService.getByAuthor((UUID) auth.getPrincipal());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EXAMINER')")
    public TemplateResponse getById(@PathVariable UUID id) {
        return templateService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EXAMINER')")
    public TemplateResponse update(@PathVariable UUID id, @RequestBody UpdateTemplateRequest req) {
        return templateService.update(id, req);
    }
}
