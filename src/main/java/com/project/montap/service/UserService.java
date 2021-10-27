package com.project.montap.service;

import com.project.montap.domain.entity.User;
import com.project.montap.domain.repository.InventoryItemRepository;
import com.project.montap.domain.repository.UserRepository;
import com.project.montap.dto.InitialInfoDto;
import com.project.montap.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    InventoryItemRepository inventoryRepository;

    // 회원 가입
    @Transactional
    public UserDto saveUser(UserDto userDto) {
        // 1. service -> repository -> DB
        // DTO -> Entity 전환 작업
//        User newUser = User
//                .builder()
//                .userId(userDto.getUserId())
//                .userPwd(userDto.getUserPwd())
//                .email(userDto.getEmail())
//                .nickname(userDto.getNickname())
//                .build();
        User newUser = new User(userDto.getUserId(), userDto.getUserPwd(), userDto.getEmail(), userDto.getNickname());
        User user = userRepository.save(newUser);

        // 3. Entity -> DTO 전환 작업
        UserDto newUserDto = user.toUserDto();

        return newUserDto;
    }

    public UserDto getUser(String userId) {
        // 1. repository -> DB -> select 문으로 해당 유저를 찾음
        User user = userRepository.findByUserId(userId);
//        System.out.println("user = " + user);

        // 2. Entity -> DTO 변환 작업
        UserDto findUser = user.toUserDto();
        return findUser;
    }

    @Transactional
    public UserDto modifyUser(UserDto userDto) {
        // 1. 사용자를 찾는다.
        User findUser = userRepository.findByUserId(userDto.getUserId());

        // 2. 찾은 Entity 에 사용자로부터 입력받는 패스워드로 변경한다.
        String newPassword = userDto.getUserPwd();
        findUser.setUserPwd(newPassword);

        // 3. DB 에 수정된 유저 정보를 저장한다. (update)
        // 4. 수정된 유저 Entity 를 받아온다.
        User user = userRepository.save(findUser);

        // 5. Entity -> Dto 변환 작업을 수행한다.
        UserDto newUser = user.toUserDto();

        return newUser;
    }

    public InitialInfoDto login(UserDto userDto) throws Exception {
        // 1. 접속한 사용자의 userId, userPwd 확인
        Optional<User> optionalUser = userRepository.findByUserIdAndUserPwd(userDto.getUserId(), userDto.getUserPwd());

        // 2. 아이디, 비밀번호가 맞는지 확인
        if (optionalUser.isEmpty()) {
            // 로그인 실패
            throw new Exception("올바른 아이디 혹은 비밀번호를 입력하세요");
        } else {
            // 로그인 성공
            User findUser = optionalUser.get();

            // 3. InitialInfoDto 생성 후 리턴
            InitialInfoDto result = InitialInfoDto
                    .builder()
                    .idx(findUser.getIdx())
                    .userId(findUser.getUserId())
                    .nickname(findUser.getNickname())
                    .email(findUser.getEmail())
                    .money(findUser.getMoney())
                    .hp(findUser.getHp())
                    .damage(findUser.getDamage())
                    .defense(findUser.getDefense())
                    .criDamage(findUser.getCriDamage())
                    .criProbability(findUser.getCriProbability())
                    .role(findUser.getRole())
                    .stage(findUser.getStage())
                    .inventoryItemList(findUser.getInventoryItemList())
                    .build();

            return result;
        }
    }
}
