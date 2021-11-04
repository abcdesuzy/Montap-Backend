package com.project.montap.controller;

import com.project.montap.dto.AuthUserDto;
import com.project.montap.dto.UserDto;
import com.project.montap.exception.Error;

import com.project.montap.service.S3Service;
import com.project.montap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    S3Service s3Service;

    // 회원가입
    @PostMapping( "/user" )
    public ResponseEntity saveUser(@RequestBody @Valid UserDto userDto) {
        UserDto newUserDto = userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(newUserDto);
    }

    // 회원조회
    @GetMapping( "/user" )
    public ResponseEntity getUser() {
        try {
            UserDto result = userService.getUser();
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }

    // 회원정보수정
    @PutMapping( "/user" )
    public ResponseEntity modifyUser(@RequestBody @Valid UserDto userDto) {
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
    @GetMapping( "/user/valid/userId/{userId}" )
    public ResponseEntity isValidUserId (@PathVariable String userId) {
        try {
/*          String userId = param.get("userId");
            System.out.println("userId = " + userId);*/
            boolean result = userService.isValidUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }

    // 닉네임 중복확인
    @PostMapping( "/user/valid/nickname" )
    public ResponseEntity isValidNickname(@RequestBody Map<String, String> param) {
        try {
            String nickname = param.get("nickname");
            System.out.println("nickname = " + nickname);
            boolean result = userService.isValidNickname(nickname);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }

    // 이메일 중복확인
    @PostMapping( "/user/valid/email" )
    public ResponseEntity isValidEmail(@RequestBody Map<String, String> param) { // KEY, VALUE
        try {
            String email = param.get("email");
            System.out.println("email = " + email);
            boolean result = userService.isValidEmail(email);
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

    // 회원 탈퇴
    @DeleteMapping("/user")
    public ResponseEntity uploadProfile(@AuthenticationPrincipal AuthUserDto authUserDto) {
        try {
            userService.deleteUser(authUserDto.getUserIdx());
            boolean result = true;
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }

    // [공통] 유효성 검사 Error 처리
    @ExceptionHandler( MethodArgumentNotValidException.class )
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
