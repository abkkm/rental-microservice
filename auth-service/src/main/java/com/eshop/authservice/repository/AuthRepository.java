package com.eshop.authservice.repository;

import com.eshop.authservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<UserEntity, Long> {

}
