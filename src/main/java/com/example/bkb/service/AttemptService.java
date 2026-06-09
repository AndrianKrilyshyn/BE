package com.example.bkb.service;

import com.example.bkb.domain.entity.Attempt;
import com.example.bkb.domain.enums.AttemptStatus;
import com.example.bkb.domain.enums.TestStatus;
import com.example.bkb.dto.AttemptResponse;
import com.example.bkb.dto.StartAttemptRequest;
import com.example.bkb.dto.StartAttemptResponse;
import com.example.bkb.exception.ApiException;
import com.example.bkb.exception.NotFoundException;
import com.example.bkb.messaging.ProctoringTaskPublisher;
import com.example.bkb.repository.AttemptRepository;
import com.example.bkb.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final TestRepository testRepository;
    private final S3PresignedUrlService s3Service;
    private final ProctoringTaskPublisher taskPublisher;

    @Transactional
    public StartAttemptResponse start(StartAttemptRequest req) {
        var test = testRepository.findByAccessCode(req.accessCode().toUpperCase())
                .orElseThrow(() -> new NotFoundException("Test not found for code: " + req.accessCode()));

        if (test.getStatus() != TestStatus.ACTIVE) {
            throw new ApiException(HttpStatus.CONFLICT, "Test is not active");
        }

        var attempt = Attempt.builder()
                .test(test)
                .candidateEmail(req.candidateEmail())
                .candidateName(req.candidateName())
                .status(AttemptStatus.IN_PROGRESS)
                .startedAt(Instant.now())
                .build();
        attemptRepository.save(attempt);

        return new StartAttemptResponse(
                attempt.getId(),
                test.getId(),
                test.getTitle(),
                test.getDurationMinutes(),
                test.getTemplate().getContentSchema(),
                attempt.getStartedAt()
        );
    }

    @Transactional
    public AttemptResponse finish(UUID attemptId, String answersJson) {
        var attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt not found"));

        if (attempt.getStatus() == AttemptStatus.COMPLETED) {
            throw new ApiException(HttpStatus.CONFLICT, "Attempt already completed");
        }

        attempt.setStatus(AttemptStatus.COMPLETED);
        attempt.setAnswersJson(answersJson);
        attemptRepository.save(attempt);

        taskPublisher.publish(attempt.getId());
        return toResponse(attempt);
    }

    public List<String> generateUploadUrls(UUID attemptId, int count) {
        if (!attemptRepository.existsById(attemptId)) {
            throw new NotFoundException("Attempt not found");
        }
        return s3Service.generateUploadUrls(attemptId, count);
    }

    private AttemptResponse toResponse(Attempt a) {
        return new AttemptResponse(a.getId(), a.getStatus(), a.getStartedAt());
    }
}
