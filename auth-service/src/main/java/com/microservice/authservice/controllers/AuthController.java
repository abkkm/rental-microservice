package com.microservice.authservice.controllers;


import com.microservice.authservice.dto.UserDto;
import com.microservice.authservice.service.AuthService;
import com.microservice.authservice.vo.request.RequestRegister;
import com.microservice.authservice.vo.response.ResponseUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@Slf4j
public class AuthController {
    private AuthService authService;
    private Environment environment;
    @Autowired
    public AuthController(AuthService authService, Environment environment){
        this.authService = authService;
        this.environment = environment;
    }
    @GetMapping("/health_check")
    public String status() {
        return String.format(
                "It's working in Auth Service"
                        + ", port(local.server.port) =" + environment.getProperty("local.server.port")
                        + ", port(server.port) =" + environment.getProperty("server.port")
                        + ", token secret = " + environment.getProperty("token.secret")
                        + ", token expiration time = " + environment.getProperty("token.exp_time")
        );
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RequestRegister user){
        UserDto userDto = authService.registerUser(UserDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUser.builder()
                        .email(userDto.getEmail())
                        .nickname(userDto.getNickname())
                        .phoneNumber(userDto.getPhoneNumber())
                        .userId(userDto.getUserId())
                        .encryptedPwd(userDto.getEncryptedPwd())
                        .build());

    }
    @GetMapping("/{userId}/getUser")
    public ResponseEntity<?> getUser(@PathVariable("userId") String userId) {
        log.info("Auth Service's Controller Layer :: Call getUser Method!");

        UserDto userDto = authService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUser.builder()
                .email(userDto.getEmail())
                .nickname(userDto.getNickname())
                .phoneNumber(userDto.getPhoneNumber())
                .userId(userDto.getUserId())
                .posts(userDto.getPosts())
                .rentals(userDto.getRentals())
                .build());
    }

    @GetMapping("/{nickname}/my-rental-list")
    public ResponseEntity<?> getMyRentals(@PathVariable("nickname") String nickname) {
        log.info("Auth Service's Controller Layer :: Call getMyRentals Method!");

        UserDto userDto = authService.getRentalsByNickname(nickname);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUser.builder().rentals(userDto.getRentals()));
    }

    @GetMapping("/{nickname}/my-borrow-list")
    public ResponseEntity<?> getMyBorrow(@PathVariable("nickname") String nickname) {
        log.info("Auth Service's Controller Layer :: Call getMyRentals Method!");

        UserDto userDto = authService.getBorrowsByNickname(nickname);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUser.builder().rentals(userDto.getRentals()));
    }
}