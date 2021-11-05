package com.project.montap.security.service;

import com.project.montap.domain.entity.User;
import com.project.montap.domain.repository.UserRepository;
import com.project.montap.dto.AuthUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service( "customUserDetailsService" )
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId);

        if (user == null) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        }

        // 인증된 유저 정보 만들기
        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setUserIdx(user.getIdx());
        authUserDto.setUserId(user.getUserId());
        authUserDto.setUserPwd(user.getUserPwd());
        authUserDto.setNickname(user.getNickname());
        authUserDto.setEmail(user.getEmail());
        authUserDto.setEmailYn(user.getEmailYn());
        authUserDto.setMoney(user.getMoney());
        authUserDto.setStage(user.getStage());
        authUserDto.setHp(user.getHp());
        authUserDto.setDefense(user.getDefense());
        authUserDto.setDamage(user.getDamage());
        authUserDto.setUserProfileUrl(user.getUserProfileUrl());

        List<GrantedAuthority> roles = new ArrayList<>();
        // TODO 나중에 권한 추가해야함
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        AccountContext accountContext = new AccountContext(authUserDto, roles);

        return accountContext;
    }
}
