package com.project.montap.security.provider;

import com.project.montap.security.service.AccountContext;
import com.project.montap.security.service.CustomUserDetailsService;
import com.project.montap.security.token.AjaxAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

public class AjaxAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 토큰에서 ID, PW 를 꺼낸다.
        String userId = authentication.getName(); // userId
        String userPwd = (String) authentication.getCredentials(); // userPwd

        AccountContext accountContext = (AccountContext) customUserDetailsService.loadUserByUsername(userId);

        // 이메일 인증을 받지 않으면 로그인 제한
        // if (accountContext.getUser().getEmailYn() == 0) throw new BadCredentialsException("이메일 인증 실패");

        // passwordEncoder.matchers 메소드 사용해서 처리
        if (!passwordEncoder.matches(userPwd, accountContext.getPassword())) {
            throw new BadCredentialsException("Invalid password : 비밀번호가 일치하지 않습니다.");
        }
        return new AjaxAuthenticationToken(accountContext.getUser(), null, accountContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AjaxAuthenticationToken.class);
    }

}
