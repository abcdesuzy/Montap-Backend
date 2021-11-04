package com.project.montap.service;

import com.project.montap.domain.entity.User;
import com.project.montap.domain.repository.InventoryItemRepository;
import com.project.montap.domain.repository.UserRepository;
import com.project.montap.dto.AuthUserDto;
import com.project.montap.dto.ModifyUserDto;
import com.project.montap.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
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

        Long userIdx = authUserDto.getUserIdx();
        Optional<User> optionalUser = userRepository.findById(userIdx);
        if (optionalUser.isEmpty()) {
            throw new Exception("해당 유저가 없습니다.");
        }
        User user = optionalUser.get();

        UserDto result = new UserDto();
        result.setIdx(user.getIdx());
        result.setUserId(user.getUserId());
        result.setNickname(user.getNickname());
        result.setEmail(user.getEmail());
        result.setMoney(user.getMoney());
        result.setStage(user.getStage());
        result.setHp(user.getHp());
        result.setDefense(user.getDefense());
        result.setDamage(user.getDamage());
        result.setUserProfileUrl(user.getUserProfileUrl());

        return result;
    }

    // 회원정보수정
    @Transactional
    public Long modifyUser(ModifyUserDto modifyUserDto) throws Exception {

        // 1. 사용자를 찾는다.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDto authUserDto = (AuthUserDto) auth.getPrincipal();
        Optional<User> optionalUser = userRepository.findById(authUserDto.getUserIdx());
        if (optionalUser.isEmpty()) {
            throw new Exception("해당 유저가 없습니다.");
        }
        User user = optionalUser.get();
        System.out.println("authUserDto = " + authUserDto);
        
        // 2. 기존 비밀번호 확인
        String existPwd = modifyUserDto.getExistPwd();
        if (!passwordEncoder.matches(existPwd, authUserDto.getUserPwd())) {
            throw new BadCredentialsException("Invalid password 비밀번호가 일치하지 않습니다.");
        }

        // 3. 비밀번호 변경인지 확인한다 + 비밀번호 확인
        String newPwd = modifyUserDto.getNewPwd();
        String confPwd = modifyUserDto.getConfPwd();
        if (newPwd != null && confPwd != null) {
            if (newPwd.equals(confPwd)) {
                user.setUserPwd(passwordEncoder.encode(existPwd));
             } else {
                throw new Exception("입력한 비밀번호가 일치하지 않습니다.");
            }
        }

        // 4. 닉네임 변경인지 확인 한다.
        String nickname = modifyUserDto.getNickname();
        if (nickname == null) {
            throw new Exception("닉네임을 작성해주세요.");
        }
        user.setNickname(nickname);
        userRepository.save(user); // DB에 반영

        return user.getIdx();

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

        System.out.println("확인 : " + userIdx);
        Optional<User> optionalUser = userRepository.findById(userIdx);

        if(optionalUser.isEmpty()){
            throw new Exception("현재 존재하지 않는 유저입니다.");
        }
            User findUser = optionalUser.get();
            userRepository.deleteById(findUser.getIdx());
    }
}