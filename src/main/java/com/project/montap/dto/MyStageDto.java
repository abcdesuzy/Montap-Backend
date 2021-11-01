package com.project.montap.dto;

import com.project.montap.enums.IsBoss;
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
