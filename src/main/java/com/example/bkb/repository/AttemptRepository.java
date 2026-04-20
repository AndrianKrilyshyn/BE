package com.example.bkb.repository;

import com.example.bkb.domain.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AttemptRepository extends JpaRepository<Attempt, UUID> {
    List<Attempt> findByStudentId(UUID studentId);
    List<Attempt> findByTestId(UUID testId);
}
