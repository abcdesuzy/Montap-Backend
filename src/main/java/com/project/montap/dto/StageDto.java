package com.project.montap.dto;

import com.project.montap.enums.IsBoss;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StageDto {
    // Stage Entity Dto
    Long idx;
    int stageCount;
    String monsterName;
    IsBoss isBoss;
    int monsterHp;
    int monsterDamage;
    int dropMoney;
    String monsterUrl;
}
