package com.project.montap.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.montap.dto.UserDto;
import com.project.montap.security.token.AjaxAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    // 로그인 경로 지정
    public AjaxLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (!isAjax(request)) {
            throw new IllegalStateException("Ajax요청이 아닙니다.");

        } else {  // 사용자로부터 받은 값을 직접 Dto로 변환해줘야 하는데 이 역할을 objectMapper 가 수행한다.
            UserDto userDto = objectMapper.readValue(request.getReader(), UserDto.class);

            if (StringUtils.isEmpty(userDto.getUserId()) || StringUtils.isEmpty(userDto.getUserPwd())) {
                throw new IllegalArgumentException("아이디나 비밀번호가 틀렸습니다.");
            }
            // 인증토큰 생성 > 인증관리자에게 토큰을 넘겨주면서 인증을 요청함
            AjaxAuthenticationToken ajaxAuthenticationToken = new AjaxAuthenticationToken(userDto.getUserId(), userDto.getUserPwd());
            return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
        }
    }

    // requset 가 Ajax 요청인지 아닌지를 판단하는 메소드

    /**
     * request header
     * X-Requested-With: XMLHttpRequest
     */
    private boolean isAjax(HttpServletRequest request) {

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return true;
        }
        return false;
    }

}
