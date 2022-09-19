package com.microservice.authservice.security;


import com.microservice.authservice.dto.UserDto;
import com.microservice.authservice.service.AuthService;
import com.microservice.authservice.vo.RequestLogin;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthService authService;
    private Environment env;

    public AuthenticationFilter(
            AuthenticationManager authenticationManager,
            AuthService authService,
            Environment env
    ) {
        super.setAuthenticationManager(authenticationManager);

        this.authService = authService;
        this.env = env;
    }

    // xác minh người dùng login bằng cách truyền vào body, json sẽ mapping thành instance của RequestLogin,
    // rồi check authenticate với method authenticate, mật khẩu tại đây cũng sẽ được mã hóa
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                    creds.getEmail(),
                    creds.getPassword(),
                    new ArrayList<>()
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        String email = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDto = authService.getUserDetailsByEmail(email);
        String token = Jwts.builder()
                .setSubject(userDto.getUserId())
                .setExpiration(new Date(System.currentTimeMillis()+Long.parseLong(env.getProperty("token.exp_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();
        response.addHeader("token", token);
        response.addHeader("userId", userDto.getUserId());
    }
}
