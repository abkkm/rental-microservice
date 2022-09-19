package com.microservice.authservice.service;

import com.microservice.authservice.dto.UserDto;
import com.microservice.authservice.entity.UserEntity;
import com.microservice.authservice.repository.AuthRepository;
import com.microservice.authservice.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.UUID;
@Service
public class AuthServiceImpl implements AuthService{
    private AuthRepository authRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository, BCryptPasswordEncoder passwordEncoder){
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
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

        if(userEntity == null) throw new UsernameNotFoundException(email);

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
        return null;
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

        if(userEntity == null) throw new UsernameNotFoundException(username);

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
