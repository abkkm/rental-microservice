package com.microservice.authservice.dto;

import com.microservice.authservice.vo.response.ResponsePost;
import com.microservice.authservice.vo.response.ResponseRental;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String userId;
    private Date createdDate;
    private String encryptedPwd;

    private List<ResponsePost> posts;

    private List<ResponseRental> rentals;

    @Builder
    public UserDto(String email,
                   String password,
                   String nickname,
                   String phoneNumber,
                   String userId,
                   Date createdDate,
                   String encryptedPwd,
                   List<ResponsePost> posts,
                   List<ResponseRental> rentals
    ) {
        this.createdDate = createdDate;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.nickname = nickname;
        this.encryptedPwd = encryptedPwd;
        this.posts = posts;
        this.rentals = rentals;
    }
}

