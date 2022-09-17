package com.eshop.authservice.repository;

import com.eshop.authservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);
}
