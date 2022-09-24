package com.microservice.authservice.entity;


import lombok.Builder;
import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 50)
    private String nickname;
    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String createdDate;
    @Column(nullable = false, unique = true)
    private String encryptedPwd;
    @Builder
    public UserEntity(
            String email,
            String nickname,
            String phoneNumber,
            String userId,
            String createdDate,
            String encryptedPwd
    ) {
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.createdDate = createdDate;
        this.encryptedPwd = encryptedPwd;
    }

    public UserEntity() {

    }
}
