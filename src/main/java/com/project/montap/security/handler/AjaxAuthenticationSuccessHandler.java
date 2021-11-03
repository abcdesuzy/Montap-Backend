package com.project.montap.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.montap.dto.AuthUserDto;
import com.project.montap.dto.InitialInfoDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setCharacterEncoding("utf8");
        AuthUserDto user = (AuthUserDto) authentication.getPrincipal();

        // 로그인시 프론트에 내려줄 유저 정보 설정 (비밀번호를 안내려주기 위함)
        InitialInfoDto initialInfoDto = new InitialInfoDto();
        initialInfoDto.setUserIdx(user.getUserIdx());
        initialInfoDto.setUserId(user.getUserId());
        initialInfoDto.setNickname(user.getNickname());
        initialInfoDto.setEmail(user.getEmail());
        initialInfoDto.setMoney(user.getMoney());
        initialInfoDto.setStage(user.getStage());
        initialInfoDto.setHp(user.getHp());
        initialInfoDto.setDefense(user.getDefense());
        initialInfoDto.setDamage(user.getDamage());
        initialInfoDto.setUserProfileUrl(user.getUserProfileUrl());


        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);


        objectMapper.writeValue(response.getWriter(), initialInfoDto);
    }
}
