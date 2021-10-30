package com.project.montap.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.montap.dto.StageDto;
import com.project.montap.enums.IsBoss;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Stage {

    @Id
    @GeneratedValue
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

    //@OneToMany(mappedBy = "stage")
    //List<StageLog> stageLogList =new ArrayList<>();
}
