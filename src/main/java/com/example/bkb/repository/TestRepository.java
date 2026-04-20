package com.example.bkb.repository;

import com.example.bkb.domain.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TestRepository extends JpaRepository<Test, UUID> {
    List<Test> findByAuthorId(UUID authorId);
}
