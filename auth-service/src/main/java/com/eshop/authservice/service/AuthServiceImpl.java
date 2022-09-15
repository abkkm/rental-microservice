package com.eshop.authservice.service;

import com.eshop.authservice.dto.UserDto;
import com.eshop.authservice.entity.UserEntity;
import com.eshop.authservice.repository.AuthRepository;
import com.eshop.authservice.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;
@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private AuthRepository repository;

    @Transactional
    @Override
    public UserDto registerUser(UserDto userDto) {
        UserEntity userEntity = UserEntity.builder()
                .email(userDto.getEmail())
                .nickname(userDto.getNickname())
                .phoneNumber(userDto.getPhoneNumber())
                .encryptedPwd("encrypted_password")
                .userId(UUID.randomUUID().toString())
                .createdDate(DateUtil.dateNow())
                .build();
        repository.save(userEntity);

        return UserDto.builder()
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .phoneNumber(userEntity.getPhoneNumber())
                .encryptedPwd(userEntity.getEncryptedPwd())
                .userId(userEntity.getUserId())
                .build();
    }
}
