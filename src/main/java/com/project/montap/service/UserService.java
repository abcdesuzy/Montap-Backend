package com.project.montap.service;

import com.project.montap.domain.entity.User;
import com.project.montap.domain.repository.InventoryItemRepository;
import com.project.montap.domain.repository.UserRepository;
import com.project.montap.dto.AuthUserDto;
import com.project.montap.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.util.Optional;

// 회원관리 서비스
@Service
@Transactional( readOnly = true )
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    InventoryItemRepository inventoryRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public UserDto saveUser(UserDto userDto) {
        // 1. service -> repository -> DB
        // 2. DTO >>> Entity 전환 작업
        // User newUser = User
        //  .builder()
        //  .userId(userDto.getUserId())
        //  .userPwd(userDto.getUserPwd())
        //  .email(userDto.getEmail())
        //  .nickname(userDto.getNickname())
        //  .build();
        User newUser = new User(
                userDto.getUserId(),
                passwordEncoder.encode(userDto.getUserPwd()),
                userDto.getNickname(), userDto.getEmail());
        User user = userRepository.save(newUser);

        // 3. Entity >>> DTO 전환 작업
        UserDto newUserDto = user.toUserDto();

        return newUserDto;
    }

    // 회원조회 - 비밀번호 빼고
    public UserDto getUser() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDto authUserDto = (AuthUserDto) auth.getPrincipal(); // 강제 형변환

        UserDto result = new UserDto();
        result.setIdx(authUserDto.getUserIdx());
        result.setUserId(authUserDto.getUserId());
        result.setNickname(authUserDto.getNickname());
        result.setEmail(authUserDto.getEmail());
        result.setMoney(authUserDto.getMoney());
        result.setStage(authUserDto.getStage());
        result.setHp(authUserDto.getHp());
        result.setDefense(authUserDto.getDefense());
        result.setDamage(authUserDto.getDamage());
        result.setUserProfileUrl(authUserDto.getUserProfileUrl());

        return result;
    }

    // 회원정보수정
    @Transactional
    public UserDto modifyUser(UserDto userDto) {

        // 1. 사용자를 찾는다.
        // 현재 로그인 한 유저의 정보 찾기
        User findUser = userRepository.findByUserId(userDto.getUserId());

        // 2. 찾은 Entity 에 사용자로부터 패스워드로 변경한다. 5~20 글자 이내
        String newPassword = passwordEncoder.encode(userDto.getUserPwd());
        findUser.setUserPwd(newPassword);

        // 3. DB 에 수정된 유저 정보를 저장한다. (update)
        // 4. 수정된 유저 Entity 를 받아온다.
        User user = userRepository.save(findUser);

        // 5. Entity >>> Dto 변환 작업을 수행한다.
        UserDto newUser = user.toUserDto();

        return newUser;
    }

    // 아이디 중복 확인
    @Transactional
    public boolean getIdCheck(String userId) throws Exception {
        User findUserId = userRepository.findByUserId(userId);
        boolean checkId = false;

        if (findUserId == null) {
            checkId = true;
        }
        return checkId;
    }

    // 닉네임 중복 확인
    @Transactional
    public boolean getNickCheck(String nickname) throws Exception {
        User findUserId = userRepository.findByNickname(nickname);
        boolean checkId = false;

        if (findUserId == null) {
            checkId = true;
        }
        return checkId;
    }

    // 이메일 중복 확인
    @Transactional
    public boolean isValidEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


    // 회원 탈퇴
}