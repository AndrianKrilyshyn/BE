package com.example.bkb.controller;

import com.example.bkb.dto.*;
import com.example.bkb.service.AttemptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('STUDENT')")
    public AttemptResponse start(@Valid @RequestBody StartAttemptRequest req, Authentication auth) {
        return attemptService.start(req.testId(), (UUID) auth.getPrincipal());
    }

    @GetMapping("/{attemptId}/video-upload-urls")
    @PreAuthorize("hasRole('STUDENT')")
    public PresignedUrlResponse getUploadUrls(@PathVariable UUID attemptId,
                                              @RequestParam(defaultValue = "5") int count) {
        return new PresignedUrlResponse(attemptService.generateUploadUrls(attemptId, count));
    }

    @PostMapping("/{attemptId}/finish")
    @PreAuthorize("hasRole('STUDENT')")
    public AttemptResponse finish(@PathVariable UUID attemptId,
                                  @RequestBody(required = false) FinishAttemptRequest req,
                                  Authentication auth) {
        var answers = req != null && req.answersJson() != null ? req.answersJson().toString() : null;
        return attemptService.finish(attemptId, (UUID) auth.getPrincipal(), answers);
    }
}
