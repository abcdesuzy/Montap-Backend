package com.project.montap.controller;

import com.project.montap.dto.InitialInfoDto;
import com.project.montap.dto.UserDto;
import com.project.montap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public ResponseEntity saveUser(@RequestBody UserDto userDto) {
        UserDto newUserDto = userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(newUserDto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity getUser(@PathVariable String userId) {
        System.out.println("userId = " + userId);
        UserDto findUser = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(findUser);
    }

    @PutMapping("/user")
    public ResponseEntity modifyUser(@RequestBody UserDto userDto) {
        UserDto newUser = userService.modifyUser(userDto);
        System.out.println("newUser = " + newUser);

        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

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
}
