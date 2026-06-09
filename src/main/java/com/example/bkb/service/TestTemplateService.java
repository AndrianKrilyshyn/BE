package com.example.bkb.service;

import com.example.bkb.domain.entity.TestTemplate;
import com.example.bkb.dto.CreateTemplateRequest;
import com.example.bkb.dto.TemplateResponse;
import com.example.bkb.dto.UpdateTemplateRequest;
import com.example.bkb.exception.NotFoundException;
import com.example.bkb.repository.TestTemplateRepository;
import com.example.bkb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestTemplateService {

    private final TestTemplateRepository templateRepository;
    private final UserRepository userRepository;

    @Transactional
    public TemplateResponse create(CreateTemplateRequest req, UUID authorId) {
        var author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found"));
        var template = TestTemplate.builder()
                .title(req.title())
                .contentSchema(req.contentSchema().toString())
                .author(author)
                .build();
        templateRepository.save(template);
        return toResponse(template);
    }

    @Transactional(readOnly = true)
    public List<TemplateResponse> getByAuthor(UUID authorId) {
        return templateRepository.findByAuthorIdOrderByCreatedAtDesc(authorId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TemplateResponse getById(UUID id) {
        return toResponse(templateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Template not found")));
    }

    @Transactional
    public TemplateResponse update(UUID id, UpdateTemplateRequest req) {
        var template = templateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Template not found"));
        if (req.title() != null) template.setTitle(req.title());
        if (req.contentSchema() != null) template.setContentSchema(req.contentSchema().toString());
        templateRepository.save(template);
        return toResponse(template);
    }

    private TemplateResponse toResponse(TestTemplate t) {
        return new TemplateResponse(t.getId(), t.getTitle(), t.getContentSchema(), t.getCreatedAt());
    }
}
