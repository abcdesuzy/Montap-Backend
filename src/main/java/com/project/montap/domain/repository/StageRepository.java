package com.project.montap.domain.repository;

import com.project.montap.domain.entity.Stage;
import com.project.montap.dto.MyStageDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StageRepository extends JpaRepository<Stage, Long> {

    @Query(nativeQuery = true)
    public List<MyStageDto> findByMyStage(@Param("userIdx") Long userIdx);

    }
