package com.example.bkb.controller;

import com.example.bkb.dto.*;
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
    @PreAuthorize("hasRole('EXAMINER')")
    public TestResponse create(@Valid @RequestBody CreateTestRequest req, Authentication auth) {
        return testService.create(req, (UUID) auth.getPrincipal());
    }

    @GetMapping
    @PreAuthorize("hasRole('EXAMINER')")
    public List<TestResponse> getMyTests(Authentication auth) {
        return testService.getByAuthor((UUID) auth.getPrincipal());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EXAMINER')")
    public TestResponse getById(@PathVariable UUID id) {
        return testService.getById(id);
    }

    @GetMapping("/by-code/{accessCode}")
    public CandidateTestResponse getByCode(@PathVariable String accessCode) {
        return testService.getByAccessCode(accessCode);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('EXAMINER')")
    public TestResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateTestRequest req) {
        return testService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('EXAMINER')")
    public void delete(@PathVariable UUID id) {
        testService.delete(id);
    }
}
