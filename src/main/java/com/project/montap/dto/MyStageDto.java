package com.project.montap.dto;

import com.project.montap.enums.IsBoss;
import lombok.*;

public interface MyStageDto {

    Long stageIdx();
    String monsterName();
    int stageCount();
    IsBoss isBoss();
    String isCleared();
}
