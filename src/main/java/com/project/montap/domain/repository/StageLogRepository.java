package com.project.montap.domain.repository;

import com.project.montap.domain.entity.StageLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface StageLogRepository extends JpaRepository<StageLog, Long> {
}
