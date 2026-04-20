package com.example.bkb.repository;

import com.example.bkb.domain.entity.ProctoringReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProctoringReportRepository extends JpaRepository<ProctoringReport, UUID> {
    Optional<ProctoringReport> findByAttemptId(UUID attemptId);
}
