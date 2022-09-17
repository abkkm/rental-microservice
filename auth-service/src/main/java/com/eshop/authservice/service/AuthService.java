package com.eshop.authservice.service;

import com.eshop.authservice.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    UserDto registerUser(UserDto userDto);

    UserDto getUserDetailsByEmail(String email);

    UserDto getUser(String userId);

    UserDto getRentalsByNickname(String nickname);

    UserDto getBorrowsByNickname(String nickname);

    boolean checkNickname(String nickname);

    boolean checkEmail(String email);
}
