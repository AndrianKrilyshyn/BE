package com.example.bkb.repository;

import com.example.bkb.domain.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AttemptRepository extends JpaRepository<Attempt, UUID> {
    List<Attempt> findByTestId(UUID testId);
    List<Attempt> findByTestAuthorId(UUID authorId);
    boolean existsByTestId(UUID testId);
}
