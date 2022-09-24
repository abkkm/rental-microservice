package com.microservice.authservice.vo.request;

import javax.validation.constraints.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class RequestLogin {
    @NotNull()
    @Size(min = 2, message = "Email không hợp lệ")
    private String email;

    @NotNull()
    @Size(min = 6, message = "Mật khẩu không nhỏ hơn 6 ký tự")
    private String password;
}
