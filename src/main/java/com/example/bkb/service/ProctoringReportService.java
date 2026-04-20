package com.example.bkb.service;

import com.example.bkb.domain.entity.ProctoringReport;
import com.example.bkb.dto.ProctoringResultMessage;
import com.example.bkb.dto.ReportResponse;
import com.example.bkb.exception.NotFoundException;
import com.example.bkb.repository.AttemptRepository;
import com.example.bkb.repository.ProctoringReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProctoringReportService {

    private final ProctoringReportRepository reportRepository;
    private final AttemptRepository attemptRepository;

    @Transactional
    public void save(ProctoringResultMessage msg) {
        var attempt = attemptRepository.findById(msg.attemptId())
                .orElseThrow(() -> new NotFoundException("Attempt not found: " + msg.attemptId()));

        var report = ProctoringReport.builder()
                .attempt(attempt)
                .trustScore(msg.trustScore())
                .totalViolations(msg.totalViolations())
                .reportMetadata(msg.reportMetadata())
                .build();
        reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public ReportResponse getByAttemptId(UUID attemptId) {
        var report = reportRepository.findByAttemptId(attemptId)
                .orElseThrow(() -> new NotFoundException("Report not found for attempt: " + attemptId));
        return toResponse(report);
    }

    private ReportResponse toResponse(ProctoringReport r) {
        return new ReportResponse(
                r.getId(),
                r.getAttempt().getId(),
                r.getTrustScore(),
                r.getTotalViolations(),
                r.getReportMetadata()
        );
    }
}
