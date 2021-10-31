package com.project.montap.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@DynamicInsert
public class StageLog {

    @Id
    @GeneratedValue
    @Column(name = "STAGELOG_IDX")
    Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_IDX")
    @JsonBackReference
    User user;

    @ManyToOne
    @JoinColumn(name = "STAGE_IDX")
    Stage stage;

    @Column(columnDefinition = "INTEGER NOT NULL DEFAULT 1")
    int isClear = 1;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime clearDate;

   public StageLog(User user, Stage stage) {
       this.user = user;
        this.stage = stage;
    }
}
