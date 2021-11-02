package com.project.montap.dto;

import com.project.montap.enums.IsBoss;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StageDto {
    Long idx;
    int stageCount;
    String monsterName;
    int monsterHp;
    int monsterDamage;
    IsBoss isBoss;
    int dropMoney;
    String monsterUrl;
}
