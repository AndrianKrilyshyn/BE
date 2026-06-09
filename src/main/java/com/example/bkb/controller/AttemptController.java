package com.example.bkb.controller;

import com.example.bkb.dto.*;
import com.example.bkb.service.AttemptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public StartAttemptResponse start(@Valid @RequestBody StartAttemptRequest req) {
        return attemptService.start(req);
    }

    @GetMapping("/{attemptId}/video-upload-urls")
    public PresignedUrlResponse getUploadUrls(@PathVariable UUID attemptId,
                                              @RequestParam(defaultValue = "5") int count) {
        return new PresignedUrlResponse(attemptService.generateUploadUrls(attemptId, count));
    }

    @PostMapping("/{attemptId}/finish")
    public AttemptResponse finish(@PathVariable UUID attemptId,
                                  @RequestBody(required = false) FinishAttemptRequest req) {
        var answers = req != null && req.answersJson() != null ? req.answersJson().toString() : null;
        return attemptService.finish(attemptId, answers);
    }
}
