package com.project.montap.controller;

import com.project.montap.dto.InitialInfoDto;
import com.project.montap.dto.UserDto;
import com.project.montap.service.S3Service;
import com.project.montap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    S3Service s3Service;

    // 회원가입
    @PostMapping("/user")
    public ResponseEntity saveUser(@RequestBody UserDto userDto) {
        UserDto newUserDto = userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(newUserDto);
    }

    // 회원조회
    @GetMapping("/user/{userId}")
    public ResponseEntity getUser(@PathVariable String userId) {
        System.out.println("userId = " + userId);
        UserDto findUser = userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(findUser);
    }

    // 회원정보수정
    @PutMapping("/user")
    public ResponseEntity modifyUser(@RequestBody UserDto userDto) {
        UserDto newUser = userService.modifyUser(userDto);
        System.out.println("newUser = " + newUser);
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserDto userDto) {
        System.out.println("userDto = " + userDto);
        try {
            InitialInfoDto result = userService.login(userDto);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }

    // 프로필 사진 변경
    @PostMapping("/user/profile") // 이미지는 param // key 값은 upload 로
    public ResponseEntity uploadProfile(@RequestParam("upload") MultipartFile file) {
        try {
            String url = s3Service.upload(file);
            return ResponseEntity.status(HttpStatus.OK).body(url);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }
}
