package com.project.montap.domain.entity;

import com.project.montap.dto.MyStageDto;
import com.project.montap.dto.StageDto;
import com.project.montap.enums.IsBoss;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@NamedNativeQuery(
        name = "Stage.findByMyStage",
        query = "select s.stage_idx,s.stage_count,s.monster_name,s.is_boss, case when sl.clear_date IS NOT NULL then TRUE else FALSE end as isCleared,s.monster_url from stage s left join stage_log sl ON s.stage_idx = sl.stage_idx and sl.user_idx = :userIdx or sl.user_idx IS NULL GROUP BY s.stage_idx"
        , resultSetMapping = "Stage.findByMyStage" )

@SqlResultSetMapping(
        name = "Stage.findByMyStage",
        classes = @ConstructorResult( targetClass = MyStageDto.class,
                columns = {@ColumnResult( name = "stage_idx", type = Long.class ),
                        @ColumnResult( name = "stage_count", type = Integer.class ),
                        @ColumnResult( name = "monster_name", type = String.class ),
                        @ColumnResult( name = "is_boss", type = String.class ),
                        @ColumnResult( name = "isCleared", type = String.class ),
                        @ColumnResult( name = "monster_url", type = String.class )} ) )

public class Stage {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "STAGE_IDX" )
    Long idx; // stageIdx
    int stageCount; // 스테이지 단계
    String monsterName; // 몬스터 이름
    @Enumerated( EnumType.STRING )
    IsBoss isBoss; // 보스 여부 : N(일반몬스터), Y(보스몬스터)
    int monsterHp; // 몬스터 체력
    int monsterDamage; // 몬스터 공격력
    int dropMoney; // 클리어 시 드랍되는 재화
    String monsterUrl; // 몬스터 이미지 주소

    public StageDto toStageDto() {
        return StageDto
                .builder()
                .idx(idx)
                .stageCount(stageCount)
                .monsterName(monsterName)
                .isBoss(isBoss)
                .monsterHp(monsterHp)
                .monsterDamage(monsterDamage)
                .dropMoney(dropMoney)
                .monsterUrl(monsterUrl)
                .build();
    }

}
