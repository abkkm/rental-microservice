package com.microservice.authservice.vo;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class RequestRegister {
    @NotNull(message = "Email không được để trống")
    @Size(min = 2, message = "Email không hợp lệ")
    private String email;

    @NotNull(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu ít nhất 6 ký tự")
    private String password;

    @NotNull(message = "Tên không được để trống")
    private String nickname;

    @NotNull(message = "Số điện thoại không được để trống")
    @Size(min = 10, message = "Số điện thoại không hợp lệ")
    private String phoneNumber;
}
