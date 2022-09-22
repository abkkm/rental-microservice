package com.microservice.authservice.repository;

import com.microservice.authservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);

    UserEntity findByNickname(String userId);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);
}
