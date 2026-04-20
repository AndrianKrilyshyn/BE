package com.example.bkb.controller;

import com.example.bkb.dto.CreateTestRequest;
import com.example.bkb.dto.TestResponse;
import com.example.bkb.service.TestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('TEACHER')")
    public TestResponse create(@Valid @RequestBody CreateTestRequest req, Authentication auth) {
        return testService.create(req, (UUID) auth.getPrincipal());
    }

    @GetMapping
    @PreAuthorize("hasRole('TEACHER')")
    public List<TestResponse> getMyTests(Authentication auth) {
        return testService.getByAuthor((UUID) auth.getPrincipal());
    }

    @GetMapping("/{id}")
    public TestResponse getById(@PathVariable UUID id) {
        return testService.getById(id);
    }
}
