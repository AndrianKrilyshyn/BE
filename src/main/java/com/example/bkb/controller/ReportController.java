package com.example.bkb.controller;

import com.example.bkb.dto.ReportResponse;
import com.example.bkb.service.ProctoringReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ProctoringReportService reportService;

    @GetMapping("/attempt/{attemptId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ReportResponse getByAttempt(@PathVariable UUID attemptId) {
        return reportService.getByAttemptId(attemptId);
    }
}
