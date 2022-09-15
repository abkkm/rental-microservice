package com.eshop.authservice.service;

import com.eshop.authservice.dto.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto);
}
