package com.project.montap.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MyStageDto {
    // 내 스테이지 정보 Dto
    Long stageIdx;
    int stageCount;
    String monsterName;
    String isBoss;
    String isCleared;
}