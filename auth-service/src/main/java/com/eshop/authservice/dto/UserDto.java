package com.eshop.authservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto {
    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String userId;
    private Date createdDate;
    private String encryptedPwd;

    @Builder
    public UserDto(String email,
                   String password,
                   String nickname,
                   String phoneNumber,
                   String userId,
                   Date createdDate,
                   String encryptedPwd) {
        this.createdDate = createdDate;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.nickname = nickname;
        this.encryptedPwd = encryptedPwd;
    }
}

