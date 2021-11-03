package com.project.montap.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.montap.dto.UserDto;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "users" )
@DynamicInsert
@ToString
public class User {

    @Id
    @GeneratedValue
    @Column( name = "USER_IDX" )
    Long idx;
    String userId;
    String userPwd;
    String nickname;
    String email;
    int money;
    int stage;
    @Column( columnDefinition = "INTEGER NOT NULL DEFAULT 100" )
    int hp = 100;
    @Column( columnDefinition = "INTEGER NOT NULL DEFAULT 0" )
    int defense = 0;
    @Column( columnDefinition = "INTEGER NOT NULL DEFAULT 10" )
    int damage = 10;
    String userProfileUrl;

    @OneToMany( mappedBy = "user" )
    @JsonManagedReference
    List<InventoryItem> inventoryItemList = new ArrayList<>();

    @OneToMany( mappedBy = "user" )
    @JsonManagedReference
    List<StageLog> stageLogList = new ArrayList<>();

    // 회원가입 
    public User(String userId, String userPwd, String nickname, String email) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.nickname = nickname;
        this.email = email;
    }

    // UserDto 창고
    public UserDto toUserDto() {
        return UserDto
                .builder()
                .idx(idx)
                .userId(userId)
                .nickname(nickname)
                .email(email)
                .money(money)
                .stage(stage)
                .hp(hp)
                .defense(defense)
                .damage(damage)
                .userProfileUrl(userProfileUrl)
                .build();
    }
}
