package com.example.bkb.service;

import com.example.bkb.domain.entity.TestTemplate;
import com.example.bkb.dto.CreateTemplateRequest;
import com.example.bkb.dto.TemplateResponse;
import com.example.bkb.exception.NotFoundException;
import com.example.bkb.repository.TestTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestTemplateService {

    private final TestTemplateRepository templateRepository;

    @Transactional
    public TemplateResponse create(CreateTemplateRequest req) {
        var template = TestTemplate.builder()
                .contentSchema(req.contentSchema().toString())
                .build();
        templateRepository.save(template);
        return toResponse(template);
    }

    @Transactional(readOnly = true)
    public TemplateResponse getById(UUID id) {
        var template = templateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Template not found"));
        return toResponse(template);
    }

    private TemplateResponse toResponse(TestTemplate t) {
        return new TemplateResponse(t.getId(), t.getContentSchema());
    }
}
