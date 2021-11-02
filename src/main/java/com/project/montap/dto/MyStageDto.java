package com.project.montap.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MyStageDto {
    Long stageIdx;
    String monsterName;
    int stageCount;
    String isBoss;
    String isCleared;
}
