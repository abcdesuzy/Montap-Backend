package com.project.montap.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AuthUserDto {
    // 시큐리티 인증용 Dto
    Long userIdx;
    String userId;
    String userPwd;
    String nickname;
    String email;
    int money;
    int stage;
    int hp;
    int defense;
    int damage;
    String userProfileUrl;
    String role;
}
