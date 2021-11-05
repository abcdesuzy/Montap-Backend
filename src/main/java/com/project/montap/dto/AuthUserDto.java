package com.project.montap.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AuthUserDto {
    // 시큐리티 인증된 유저 정보 Dto
    Long userIdx;
    String userId;
    String userPwd;
    String nickname;
    String email;
    int emailYn;
    int money;
    int stage;
    int hp;
    int defense;
    int damage;
    String userProfileUrl;
}
