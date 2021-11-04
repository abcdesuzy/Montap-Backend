package com.project.montap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserDto {
    // 회원정보변경 Dto
    String existPwd; // 기존 비밀번호
    String newPwd;  // 새 비밀번호
    String confPwd; // 비밀번호 확인
    String nickname;
}
