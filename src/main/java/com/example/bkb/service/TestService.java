package com.example.bkb.service;

import com.example.bkb.domain.entity.Test;
import com.example.bkb.domain.enums.TestStatus;
import com.example.bkb.dto.*;
import com.example.bkb.exception.ApiException;
import com.example.bkb.exception.NotFoundException;
import com.example.bkb.repository.AttemptRepository;
import com.example.bkb.repository.TestRepository;
import com.example.bkb.repository.TestTemplateRepository;
import com.example.bkb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestService {

    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final TestRepository testRepository;
    private final TestTemplateRepository templateRepository;
    private final UserRepository userRepository;
    private final AttemptRepository attemptRepository;

    @Transactional
    public TestResponse create(CreateTestRequest req, UUID authorId) {
        var template = templateRepository.findById(req.templateId())
                .orElseThrow(() -> new NotFoundException("Template not found"));
        var author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found"));

        var test = Test.builder()
                .template(template)
                .title(req.title())
                .author(author)
                .accessCode(generateUniqueCode())
                .status(TestStatus.ACTIVE)
                .durationMinutes(req.durationMinutes())
                .build();
        testRepository.save(test);
        return toResponse(test);
    }

    @Transactional(readOnly = true)
    public List<TestResponse> getByAuthor(UUID authorId) {
        return testRepository.findByAuthorId(authorId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TestResponse getById(UUID id) {
        return toResponse(testRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Test not found")));
    }

    @Transactional(readOnly = true)
    public CandidateTestResponse getByAccessCode(String accessCode) {
        var test = testRepository.findByAccessCode(accessCode.toUpperCase())
                .orElseThrow(() -> new NotFoundException("Test not found"));
        return new CandidateTestResponse(test.getId(), test.getTitle(), test.getDurationMinutes(), test.getStatus());
    }

    @Transactional
    public TestResponse update(UUID id, UpdateTestRequest req) {
        var test = testRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Test not found"));
        if (req.title() != null) test.setTitle(req.title());
        if (req.durationMinutes() != null) test.setDurationMinutes(req.durationMinutes());
        if (req.status() != null) test.setStatus(req.status());
        testRepository.save(test);
        return toResponse(test);
    }

    @Transactional
    public void delete(UUID id) {
        if (!testRepository.existsById(id)) {
            throw new NotFoundException("Test not found");
        }
        if (attemptRepository.existsByTestId(id)) {
            throw new ApiException(HttpStatus.CONFLICT, "Cannot delete test with existing attempts");
        }
        testRepository.deleteById(id);
    }

    private TestResponse toResponse(Test t) {
        return new TestResponse(
                t.getId(),
                t.getTemplate().getId(),
                t.getTitle(),
                t.getAccessCode(),
                t.getStatus(),
                t.getAuthor().getId(),
                t.getDurationMinutes()
        );
    }

    private String generateUniqueCode() {
        String code;
        do {
            var sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CODE_CHARS.charAt(RANDOM.nextInt(CODE_CHARS.length())));
            }
            code = sb.toString();
        } while (testRepository.existsByAccessCode(code));
        return code;
    }
}
