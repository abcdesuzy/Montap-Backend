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
@Transactional(readOnly = true)
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
                userDto.getUserId(), // 5~15 글자 이내
                passwordEncoder.encode(userDto.getUserPwd()), // 5~20 글자 이내
                userDto.getNickname(), userDto.getEmail()); // 2~8 글자 이내
        User user = userRepository.save(newUser);

        // 3. Entity >>> DTO 전환 작업
        UserDto newUserDto = user.toUserDto();

        return newUserDto;
    }

    // 로그인
    public AuthUserDto login(UserDto userDto) throws Exception {

        // 1. 접속한 사용자의 userId, userPwd 확인
        Optional<User> optionalUser = userRepository.findByUserIdAndUserPwd(userDto.getUserId(), userDto.getUserPwd());

        // 2. 아이디, 비밀번호가 맞는지 확인
        if (optionalUser.isEmpty()) {
            // 로그인 실패
            throw new Exception("아이디 혹은 비밀번호를 다시 입력하세요.");
        } else {
            // 로그인 성공하면 유저 가져오기
            User findUser = optionalUser.get();

            // 현재 로그인 한 유저의 정보 찾기
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            AuthUserDto authUserDto = (AuthUserDto) auth.getPrincipal(); // 강제 형변환
            System.out.println("테스트 : " + authUserDto);

            // 3. InitialInfoDto 생성 후 리턴
            AuthUserDto result = AuthUserDto
                    .builder()
                    .userIdx(findUser.getIdx())
                    .userId(findUser.getUserId())
                    .nickname(findUser.getNickname())
                    .email(findUser.getEmail())
                    .money(findUser.getMoney())
                    .stage(findUser.getStage())
                    .hp(findUser.getHp())
                    .damage(findUser.getDamage())
                    .defense(findUser.getDefense())
                    .userProfileUrl(findUser.getUserProfileUrl())
                    .build();

            return result;
        }
    }

    // 회원조회
    public User getUser(Long userIdx) throws Exception {

        // 1. Repository >> DB >> SELECT 문으로 해당 유저를 찾음
        Optional<User> optionalUser = userRepository.findById(userIdx);
       // User user = userRepository.findByUserId(userId);
        System.out.println("user = " + optionalUser);

        if (optionalUser.isEmpty()) {
            throw new Exception("해당하는 유저가 없습니다.");
        }
        User user = optionalUser.get();
        User result = userRepository.findByUserId(user.getUserId());


//        // 2. Entity >>> DTO 변환 작업
//        UserDto findUser = user.toUserDto();

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
    public boolean getIdCheck(String userId) throws Exception{
        User findUserId = userRepository.findByUserId(userId);
        boolean checkId = false;

        if(findUserId == null) {
            checkId = true;
        }
        return checkId;
    }

    // 닉네임 중복 확인
    @Transactional
    public boolean getNickCheck(String nickname) throws Exception{
        User findUserId = userRepository.findByNickname(nickname);
        boolean checkId = false;

        if(findUserId == null) {
            checkId = true;
        }
        return checkId;
    }

    // 이메일 중복 확인


}