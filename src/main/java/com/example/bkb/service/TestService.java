package com.example.bkb.service;

import com.example.bkb.domain.entity.Test;
import com.example.bkb.dto.CreateTestRequest;
import com.example.bkb.dto.TestResponse;
import com.example.bkb.exception.NotFoundException;
import com.example.bkb.repository.TestRepository;
import com.example.bkb.repository.TestTemplateRepository;
import com.example.bkb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final TestTemplateRepository templateRepository;
    private final UserRepository userRepository;

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
                .startTime(req.startTime())
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

    private TestResponse toResponse(Test t) {
        return new TestResponse(
                t.getId(),
                t.getTemplate().getId(),
                t.getTitle(),
                t.getAuthor().getId(),
                t.getStartTime(),
                t.getDurationMinutes()
        );
    }
}
