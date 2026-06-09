package com.example.bkb.controller;

import com.example.bkb.dto.AttemptDetailResponse;
import com.example.bkb.dto.AttemptSummaryResponse;
import com.example.bkb.dto.ReportResponse;
import com.example.bkb.service.ProctoringReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ProctoringReportService reportService;

    @GetMapping("/attempts")
    @PreAuthorize("hasRole('EXAMINER')")
    public List<AttemptSummaryResponse> getAllAttempts(Authentication auth) {
        return reportService.getAllForExaminer((UUID) auth.getPrincipal());
    }

    @GetMapping("/tests/{testId}")
    @PreAuthorize("hasRole('EXAMINER')")
    public List<AttemptSummaryResponse> getAttemptsByTest(@PathVariable UUID testId) {
        return reportService.getAllForTest(testId);
    }

    @GetMapping("/attempt/{attemptId}")
    @PreAuthorize("hasRole('EXAMINER')")
    public ReportResponse getReportByAttempt(@PathVariable UUID attemptId) {
        return reportService.getByAttemptId(attemptId);
    }

    @GetMapping("/attempt/{attemptId}/detail")
    @PreAuthorize("hasRole('EXAMINER')")
    public AttemptDetailResponse getAttemptDetail(@PathVariable UUID attemptId) {
        return reportService.getAttemptDetail(attemptId);
    }
}
