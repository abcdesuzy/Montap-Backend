package com.project.montap.security.config;

import com.project.montap.security.filter.AjaxLoginProcessingFilter;
import com.project.montap.security.handler.AjaxAuthenticationSuccessHandler;
import com.project.montap.security.provider.AjaxAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // 설정파일
@EnableWebSecurity // 스프링 시큐리티
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override // 1. 실제 시큐리티 설정을 해주는 메소드
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // 리액트, rest api 이므로 기본설정 사용 안함. 기본설정 비인증시 로그인폼 화면으로 redirect 된다.
                .csrf().disable() // 불필요하므로 사용 안함
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user").permitAll() // 회원가입 모두 허용
                .antMatchers(HttpMethod.POST, "/user/valid/userid").permitAll() // 아이디 중복 체크 모두 허용
                .antMatchers(HttpMethod.POST, "/user/valid/nickname").permitAll() // 닉네임 중복 체크 모두 허용
                .antMatchers(HttpMethod.POST, "/login").permitAll() // 로그인 모두 허용\
                .antMatchers(HttpMethod.POST, "/user/valid/email").permitAll() // 로그인 모두 허용
                .antMatchers("/").permitAll()// 초기화면 모두 허용
                .anyRequest().authenticated() // 위에  3개 외에 모든 요청들은 인증이 필요하다.
                .and()
                .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class); // 필터 추가
    }

    // 인증매니져 얻어오는 메소드
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ajaxAuthenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AjaxAuthenticationSuccessHandler();
    }

    @Bean
    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
        return ajaxLoginProcessingFilter;
    }

    @Bean
    public AjaxAuthenticationProvider ajaxAuthenticationProvider() {
        return new AjaxAuthenticationProvider();
    }
}
