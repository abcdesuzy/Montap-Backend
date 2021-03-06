package com.project.montap.controller;

import com.project.montap.dto.AuthUserDto;
import com.project.montap.dto.ModifyUserDto;
import com.project.montap.dto.UserDto;
import com.project.montap.exception.Error;
import com.project.montap.service.ConfirmationTokenService;
import com.project.montap.service.S3Service;
import com.project.montap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin( origins = "*", allowedHeaders = "*" )
@RestController
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    S3Service s3Service;

    @Autowired
    ConfirmationTokenService confirmationTokenService;

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
    public ResponseEntity modifyUser(@RequestBody @Valid ModifyUserDto modifyUserDto) {
        try {
            Long result = userService.modifyUser(modifyUserDto);
//            if (modifyUserDto.getNewPwd() != null && modifyUserDto.getConfPwd() != null) {
//                Authentication authentication = authenticationManager.authenticate(new AjaxAuthenticationToken(result, modifyUserDto.getNewPwd()));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }

    }

    // 아이디 중복확인
    @GetMapping( "/user/valid/userId/{userId}" )
    public ResponseEntity isValidUserId(@PathVariable String userId) {
        try {
            if (userId.length() < 5 || userId.length() > 15) throw new Exception("아이디는 5-15 글자로 입력해주세요.");

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
            if (nickname.length() > 1 && nickname.length() < 9) {
                boolean result = userService.isValidNickname(nickname);
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                throw new Exception("닉네임은 2-8 글자로 입력해주세요.");
            }
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


    //로그아웃
    @PostMapping( "/logout" )
    public ResponseEntity<String> logout(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) {
        httpSession.invalidate();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, auth);
        String str = "로그아웃 되었습니다.";
        return ResponseEntity.status(HttpStatus.OK).body(str);
    }

    // 회원 탈퇴
    @DeleteMapping( "/user" )
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

    // E-mail 을 보내는 API
    @PostMapping( "/email" )
    public ResponseEntity sendEmail(@RequestBody Map<String, String> param) {
        try {
            String email = param.get("email");
            String userId = param.get("userId");
            confirmationTokenService.createEmailConfirmationToken(email, userId);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }

    // 링크 클릭 시 호출되는 API
    @GetMapping( "/confirm/email/{token}" )
    public ResponseEntity viewConfirmEmail(@PathVariable Long token) {
        try {
            System.out.println("token = " + token);
            userService.confirmEmail(token);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error(e.getMessage()));
        }
    }

}
