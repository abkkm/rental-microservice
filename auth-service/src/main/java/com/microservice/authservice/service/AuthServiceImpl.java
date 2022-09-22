package com.microservice.authservice.service;

import com.microservice.authservice.clients.PostsClient;
import com.microservice.authservice.clients.RentalClient;
import com.microservice.authservice.dto.UserDto;
import com.microservice.authservice.entity.UserEntity;
import com.microservice.authservice.repository.AuthRepository;
import com.microservice.authservice.util.DateUtil;
import com.microservice.authservice.vo.response.ResponsePost;
import com.microservice.authservice.vo.response.ResponseRental;
import com.microservice.authservice.vo.response.ResponseUser;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private AuthRepository authRepository;
    private BCryptPasswordEncoder passwordEncoder;

    private PostsClient postClient;

    private RentalClient rentalClient;

    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           PostsClient postClient,
                           RentalClient rentalClient,
                           CircuitBreakerFactory circuitBreakerFactory) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.postClient = postClient;
        this.rentalClient = rentalClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Transactional
    @Override
    public UserDto registerUser(UserDto userDto) {
        UserEntity userEntity = UserEntity.builder()
                .email(userDto.getEmail())
                .nickname(userDto.getNickname())
                .phoneNumber(userDto.getPhoneNumber())
                .encryptedPwd(passwordEncoder.encode(userDto.getPassword()))
                .userId(UUID.randomUUID().toString())
                .createdDate(DateUtil.dateNow())
                .build();
        authRepository.save(userEntity);
        return UserDto.builder()
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .phoneNumber(userEntity.getPhoneNumber())
                .encryptedPwd(userEntity.getEncryptedPwd())
                .userId(userEntity.getUserId())
                .build();
    }

    @Transactional
    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = authRepository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);

        return UserDto.builder()
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .phoneNumber(userEntity.getPhoneNumber())
                .userId(userEntity.getUserId())
                .encryptedPwd(userEntity.getEncryptedPwd())
                .build();
    }

    @Override
    public UserDto getUser(String userId) {
        UserEntity userEntity = authRepository.findByUserId(userId);

        if (userEntity == null) throw new UsernameNotFoundException(userId);
        ///Chặn các repeated calls vào service khi có lỗi, ví dụ như cái post service lỗi, nó sẽ mặc định trả ra rỗng, cái này xử dụng khi 1 service bị lỗi
        log.info("Before call post-service");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");

        List<ResponsePost> postList = circuitBreaker.run(
                () -> postClient.getPosts(userId),
                throwable -> new ArrayList<>()
        );

        List<ResponseRental> rentalList = circuitBreaker.run(
                () -> rentalClient.getRentalsByOwner(userId),
                throwable -> new ArrayList<>()
        );
        log.info("After called post-service");
        return UserDto.builder()
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .phoneNumber(userEntity.getPhoneNumber())
                .userId(userEntity.getUserId())
                .encryptedPwd(userEntity.getEncryptedPwd())
                .posts(postList) /// lấy ra các bài post của mình
                .rentals(rentalList) /// lấy ra rental của mình
                .build();
    }

    @Override
    public UserDto getRentalsByNickname(String nickname) {
        return null;
    }

    @Override
    public UserDto getBorrowsByNickname(String nickname) {
        return null;
    }

    @Override
    public boolean checkNickname(String nickname) {
        return false;
    }

    @Override
    public boolean checkEmail(String email) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = authRepository.findByEmail(username);

        if (userEntity == null) throw new UsernameNotFoundException(username);

        return new User(
                userEntity.getEmail(),
                userEntity.getEncryptedPwd(),
                true,
                true,
                true,
                true,
                new ArrayList<>()
        );
    }
}
