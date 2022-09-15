package com.eshop.authservice.vo;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RequestLogin {
    @NotNull(message = "Email không được để trống")
    @Size(min = 2, message = "Email không hợp lệ")
    private String email;

    @NotNull(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu không nhỏ hơn 6 ký tự")
    private String password;
}
