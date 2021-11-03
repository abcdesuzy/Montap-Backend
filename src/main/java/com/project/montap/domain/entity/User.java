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

    // @NotNull(message = "아이디를 입력해주세요.")
    // @Size( min = 5, max = 15 )
    String userId;

    // @NotNull(message = "비밀번호를 입력해주세요.")
    // @Size( min = 5, max = 20 )
    String userPwd;

    //@NotNull(message = "닉네임을 입력해주세요.")
    // @Size( min = 2, max = 8 )
    String nickname;

    // @NotNull(message = "이메일을 형식에 맞게 입력해주세요.")
    // @Pattern(regexp = "[a-zA-z0-9]+@[a-zA-z]+[.]+[a-zA-z.]+")
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
    String role;

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
                .role(role)
                .build();
    }
}
