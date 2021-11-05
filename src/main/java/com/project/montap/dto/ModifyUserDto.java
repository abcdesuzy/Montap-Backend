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
    String existPwd; // 기존 비밀번호 ( 필수 입력 조건 )
    String newPwd;  // 새 비밀번호 ( 비밀번호 변경할 때만 입력 )
    String confPwd; // 비밀번호 확인 ( 비밀번호 변경할 때만 입력 )
    String nickname; // 닉네임 변경 ( null 값 허용 안함 )
}
