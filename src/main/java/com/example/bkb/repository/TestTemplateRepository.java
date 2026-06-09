package com.example.bkb.repository;

import com.example.bkb.domain.entity.TestTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TestTemplateRepository extends JpaRepository<TestTemplate, UUID> {
    List<TestTemplate> findByAuthorIdOrderByCreatedAtDesc(UUID authorId);
}
