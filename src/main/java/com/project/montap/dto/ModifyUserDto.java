package com.project.montap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserDto {
    // 회원정보변경 Dto
    @Size( min = 5, max = 20, message = "비밀번호는 5-20 글자로 입력해주세요." )
    String existPwd; // 기존 비밀번호 ( 필수 입력 조건 )
    @Size( min = 5, max = 20, message = "비밀번호는 5-20 글자로 입력해주세요." )
    String newPwd;  // 새 비밀번호 ( 비밀번호 변경할 때만 입력 )
    @Size( min = 5, max = 20, message = "비밀번호는 5-20 글자로 입력해주세요." )
    String confPwd; // 비밀번호 확인 ( 비밀번호 변경할 때만 입력 )
    @Size( min = 2, max = 8, message = "닉네임은 2-8 글자로 입력해주세요." )
    String nickname; // 닉네임 변경 ( null 값 허용 안함 )
}
