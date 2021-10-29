package com.project.montap.security.service;

import com.project.montap.dto.AuthUserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AccountContext extends User {


    private final AuthUserDto user;

    public AccountContext(AuthUserDto user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUserId(), user.getUserPwd(), authorities);
        this.user = user;
    }

    public AuthUserDto getUser() {
        return user;
    }
}
