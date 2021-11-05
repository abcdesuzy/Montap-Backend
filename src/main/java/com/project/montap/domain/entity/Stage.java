package com.project.montap.domain.entity;


import com.project.montap.dto.MyStageDto;

import com.project.montap.dto.StageDto;
import com.project.montap.enums.IsBoss;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@NamedNativeQuery(
        name = "Stage.findByMyStage",
        query = "select s.stage_idx, s.monster_name, s.stage_count,s.is_boss, case when sl.clear_date IS NOT NULL then TRUE else FALSE end as isCleared from stage s left join stage_log sl ON s.stage_idx = sl.stage_idx WHERE sl.user_idx = :userIdx or sl.user_idx IS NULL GROUP BY s.stage_idx"
                ,resultSetMapping = "Stage.findByMyStage"
                )

@SqlResultSetMapping(
                name="Stage.findByMyStage",
                classes = @ConstructorResult( targetClass = MyStageDto.class,
                columns = { @ColumnResult(name="stage_idx",type=Long.class),
                            @ColumnResult(name="monster_name",type=String.class),
                            @ColumnResult(name="stage_count",type=Integer.class),
                            @ColumnResult(name="is_boss",type=String.class),
                            @ColumnResult(name="isCleared",type=String.class)}) )

public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "STAGE_IDX")
    Long idx;
    int stageCount;
    String monsterName;
    int monsterHp;
    int monsterDamage;
    @Enumerated(EnumType.STRING)
    IsBoss isBoss;

    int dropMoney;
    String monsterUrl;

     public StageDto toStageDto(){
         return StageDto
                 .builder()
                 .idx(idx)
                 .stageCount(stageCount)
                 .monsterName(monsterName)
                 .monsterHp(monsterHp)
                 .monsterDamage(monsterDamage)
                 .isBoss(isBoss)
                 .dropMoney(dropMoney)
                 .monsterUrl(monsterUrl)
                 .build();
    }

}
