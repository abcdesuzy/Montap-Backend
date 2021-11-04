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
        AuthUserDto authUserDto = (AuthUserDto) auth.getPrincipal();


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

        // 2. 찾은 Entity 에 사용자로부터 받은 닉네임, 패스워드로 변경한다.
        String newNickname = userDto.getNickname();
        findUser.setNickname(newNickname);

        String newPassword = passwordEncoder.encode(userDto.getUserPwd());
        findUser.setUserPwd(newPassword);

        // 3. DB 에 수정된 유저 정보를 저장한다. (update)
        // 4. 수정된 유저 Entity 를 받아온다.
        User user = userRepository.save(findUser);

        // 5. Entity >>> Dto 변환 작업을 수행한다.
        UserDto newUser = user.toUserDto();

        return newUser;
    }

    // 아이디 중복 확인 isValidUserId
    @Transactional
    public boolean isValidUserId(String userId) throws Exception {
        User user = userRepository.findByUserId(userId);
        System.out.println("Test user : " + user);
        if (user == null) {
            return true;
        } else {
            return false;
        }
    }

    // 닉네임 중복 확인 isValidNick
    @Transactional
    public boolean isValidNickname(String nickname) {
        Optional<User> optionalUser = userRepository.findByNickname(nickname);
        if (optionalUser.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    // 이메일 중복 확인 isValidEmail
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
    @Transactional
    public void deleteUser (Long userIdx) throws Exception{

        System.out.println("확인" + userIdx);
        Optional<User> optionalUser = userRepository.findById(userIdx);

        if(optionalUser.isEmpty()){
            throw new Exception("현재 존재하지 않는 유저입니다.");
        }
            User findUser = optionalUser.get();
            userRepository.deleteById(findUser.getIdx());
    }
}