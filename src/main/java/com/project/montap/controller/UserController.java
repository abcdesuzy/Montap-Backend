package com.project.montap.controller;

import com.project.montap.domain.entity.User;
import com.project.montap.dto.AuthUserDto;
import com.project.montap.dto.UserDto;
import com.project.montap.exception.Error;
import com.project.montap.service.S3Service;
import com.project.montap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    S3Service s3Service;

    // 회원가입
    @PostMapping( "/user" )
    public ResponseEntity saveUser(@RequestBody UserDto userDto) {
        UserDto newUserDto = userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(newUserDto);
    }

    // 로그인
    @PostMapping( "/login" )
    public ResponseEntity login(@RequestBody UserDto userDto) {
        System.out.println("userDto = " + userDto);
        try {
            AuthUserDto result = userService.login(userDto);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }

    // 회원조회
    @GetMapping( "/user" )
    public ResponseEntity getUser(@AuthenticationPrincipal AuthUserDto authUserDto) throws Exception {
        try {
            System.out.println("userId = " + authUserDto);
            User result = userService.getUser(authUserDto.getUserIdx());
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }

    // 회원정보수정
    @PutMapping( "/user" )
    public ResponseEntity modifyUser(@RequestBody UserDto userDto) throws Exception {
        try {
            UserDto newUser = userService.modifyUser(userDto);
            System.out.println("newUser = " + newUser);
            return ResponseEntity.status(HttpStatus.OK).body(newUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }

    }

    // 아이디 중복확인
    @GetMapping( "/user/idcheck/{userId}" )
    public ResponseEntity userIdCheck(@PathVariable String userId) {
        try {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null; //ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error (e.getMessage()));
        }
    }

    // 닉네임 중복확인
    @GetMapping( "/user/nickcheck/{userId}" )
    public ResponseEntity userNickCheck(@PathVariable String userId) {
        try {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null; //ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error (e.getMessage()));
        }
    }

    // 이메일 중복확인
    @GetMapping( "/user/emailcheck/{userId}" )
    public ResponseEntity userEmailCheck(@PathVariable String userId) {
        try {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null; //ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error (e.getMessage()));
        }
    }

    // 프로필 사진 변경
    @PostMapping( "/user/profile" ) // 이미지는 param // key 값은 upload 로
    public ResponseEntity uploadProfile(@RequestParam( "upload" ) MultipartFile file) {
        try {
            String url = s3Service.upload(file);
            return ResponseEntity.status(HttpStatus.OK).body(url);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }
}
