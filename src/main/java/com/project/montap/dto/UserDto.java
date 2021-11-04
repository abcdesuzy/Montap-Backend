package com.project.montap.dto;

import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDto {
    Long idx;

    @Size( min = 5, max = 15, message = "아이디는 5-15 글자로 입력해주세요." )
    String userId;

    @Size( min = 5, max = 20, message = "비밀번호는 5-20 글자로 입력해주세요." )
    String userPwd;

    @Size( min = 2, max = 8, message = "닉네임은 2-8 글자로 입력해주세요." )
    String nickname;

    @Pattern( regexp = "[a-zA-z0-9]+@[a-zA-z]+[.]+[a-zA-z.]+", message = "이메일을 형식에 맞게 입력해주세요." )
    String email;

    int money;
    int stage;
    int hp;
    int defense;
    int damage;
    String userProfileUrl;
}
