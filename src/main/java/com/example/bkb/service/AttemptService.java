package com.example.bkb.service;

import com.example.bkb.domain.entity.Attempt;
import com.example.bkb.domain.enums.AttemptStatus;
import com.example.bkb.dto.AttemptResponse;
import com.example.bkb.exception.ApiException;
import com.example.bkb.exception.ForbiddenException;
import com.example.bkb.exception.NotFoundException;
import com.example.bkb.messaging.ProctoringTaskPublisher;
import com.example.bkb.repository.AttemptRepository;
import com.example.bkb.repository.TestRepository;
import com.example.bkb.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final S3PresignedUrlService s3Service;
    private final ProctoringTaskPublisher taskPublisher;

    @Transactional
    public AttemptResponse start(UUID testId, UUID studentId) {
        var test = testRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Test not found"));
        var student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        var attempt = Attempt.builder()
                .test(test)
                .student(student)
                .status(AttemptStatus.IN_PROGRESS)
                .startedAt(Instant.now())
                .build();
        attemptRepository.save(attempt);
        return toResponse(attempt);
    }

    @Transactional
    public AttemptResponse finish(UUID attemptId, UUID studentId, String answersJson) {
        var attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt not found"));

        if (!attempt.getStudent().getId().equals(studentId)) {
            throw new ForbiddenException("Not your attempt");
        }
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
