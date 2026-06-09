package com.example.bkb.service;

import com.example.bkb.domain.entity.ProctoringReport;
import com.example.bkb.dto.*;
import com.example.bkb.exception.NotFoundException;
import com.example.bkb.repository.AttemptRepository;
import com.example.bkb.repository.ProctoringReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProctoringReportService {

    private final ProctoringReportRepository reportRepository;
    private final AttemptRepository attemptRepository;
    private final S3PresignedUrlService s3Service;

    @Transactional
    public void save(ReportData data) {
        var attempt = attemptRepository.findById(data.attemptId())
                .orElseThrow(() -> new NotFoundException("Attempt not found: " + data.attemptId()));

        var report = ProctoringReport.builder()
                .attempt(attempt)
                .data(data)
                .build();
        reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public ReportResponse getByAttemptId(UUID attemptId) {
        var report = reportRepository.findByAttemptId(attemptId)
                .orElseThrow(() -> new NotFoundException("Report not found for attempt: " + attemptId));
        return toReportResponse(report);
    }

    @Transactional(readOnly = true)
    public List<AttemptSummaryResponse> getAllForExaminer(UUID examinerId) {
        return attemptRepository.findByTestAuthorId(examinerId).stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AttemptSummaryResponse> getAllForTest(UUID testId) {
        return attemptRepository.findByTestId(testId).stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public AttemptDetailResponse getAttemptDetail(UUID attemptId) {
        var attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt not found"));
        var report = reportRepository.findByAttemptId(attemptId)
                .map(this::toReportResponse)
                .orElse(null);

        return new AttemptDetailResponse(
                attempt.getId(),
                attempt.getCandidateEmail(),
                attempt.getCandidateName(),
                attempt.getStatus(),
                attempt.getStartedAt(),
                attempt.getAnswersJson(),
                attempt.getTest().getTemplate().getContentSchema(),
                report
        );
    }

    private ReportResponse toReportResponse(ProctoringReport report) {
        var data = report.getData();
        var events = data.events().stream()
                .map(this::toEventResponse)
                .toList();
        return new ReportResponse(
                report.getId(),
                report.getAttempt().getId(),
                data.trustScore(),
                data.totalViolations(),
                data.recordingDurationSec(),
                events
        );
    }

    private EventResponse toEventResponse(ReportEvent event) {
        var violations = event.violations() == null ? List.<ViolationResponse>of()
                : event.violations().stream().map(this::toViolationResponse).toList();
        return new EventResponse(
                event.startTimeSec(),
                event.endTimeSec(),
                event.severity(),
                event.confidence(),
                presignOrNull(event.clipKey()),
                presignOrNull(event.thumbnailKey()),
                violations
        );
    }

    private String presignOrNull(String key) {
        return (key == null || key.isBlank()) ? null : s3Service.generateDownloadUrl(key);
    }

    private ViolationResponse toViolationResponse(Violation v) {
        return new ViolationResponse(
                v.category(),
                v.startTimeSec(),
                v.endTimeSec(),
                v.severity(),
                v.confidence()
        );
    }

    private AttemptSummaryResponse toSummary(com.example.bkb.domain.entity.Attempt a) {
        var trustScore = reportRepository.findByAttemptId(a.getId())
                .map(r -> r.getData().trustScore())
                .orElse(null);
        return new AttemptSummaryResponse(
                a.getId(),
                null,
                a.getCandidateEmail(),
                a.getCandidateName(),
                a.getTest().getTitle(),
                null,
                trustScore,
                a.getStatus()
        );
    }
}
