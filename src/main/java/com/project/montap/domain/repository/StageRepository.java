package com.project.montap.domain.repository;

import com.project.montap.domain.entity.Stage;
import com.project.montap.domain.entity.StageLog;
import com.project.montap.dto.MyStageDto;
import com.project.montap.dto.StageDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface StageRepository extends JpaRepository<Stage, Long> {
    @Query("select s.idx, s.monsterName, s.stageCount,s.isBoss, case when sl.clearDate IS NOT NULL then TRUE else FALSE end as isCleared from Stage s left join StageLog sl" +
            " ON s.idx = sl.stage.idx WHERE sl.user.idx = ?1 or sl.user.idx IS NULL GROUP BY s.idx")
    public List<MyStageDto> findByMyStage(Long userIdx);
    }
