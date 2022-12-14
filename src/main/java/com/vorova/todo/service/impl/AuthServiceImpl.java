package com.vorova.todo.service.impl;

import com.vorova.todo.models.dto.JwtRequestDto;
import com.vorova.todo.models.dto.JwtResponseDto;
import com.vorova.todo.models.entity.User;
import com.vorova.todo.service.abstracts.AuthService;
import com.vorova.todo.service.abstracts.JwtService;
import com.vorova.todo.service.abstracts.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    private final Map<String, String> refreshStorage = new HashMap<>();

    @Autowired
    public AuthServiceImpl(UserService userService, PasswordEncoder encoder, JwtService jwtService) {
        this.userService = userService;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    @Override
    public JwtResponseDto login(JwtRequestDto requestDto) throws AuthException {
        User user = userService.getByEmail(requestDto.getEmail())
            .orElseThrow(() -> new AuthException("User with email " + requestDto.getEmail() + " not found"));
        if(encoder.matches(requestDto.getPassword(), user.getPassword())) {
            final String accessesToken = jwtService.generateAccessToken(user);
            final String refreshToken = jwtService.generateRefreshToken(user);
            refreshStorage.put(user.getEmail(), refreshToken);
            return new JwtResponseDto(accessesToken, refreshToken);
        } else {
            throw new AuthException("In correct password");
        }
    }

    @Override
    public JwtResponseDto getAccessToken(String refreshToken) throws AuthException {
        if(jwtService.validateRefreshToken(refreshToken)) {
            Claims claims = jwtService.getRefreshClaims(refreshToken);
            String email = claims.getSubject();
            String saveRefreshStorage = refreshStorage.get(email);
            if(saveRefreshStorage != null && saveRefreshStorage.equals(refreshToken)) {
                User user = userService.getByEmail(email)
                        .orElseThrow(() -> new AuthException(("User with name " + email + " not found")));
                String accessToken = jwtService.generateAccessToken(user);
                return new JwtResponseDto(accessToken, null);
            }
            throw new AuthException("Invalid refresh token");
        }
        return new JwtResponseDto(null, null);
    }

    @Override
    public JwtResponseDto refresh(String refreshToken) throws AuthException {
        if(jwtService.validateRefreshToken(refreshToken)) {
            Claims claims = jwtService.getRefreshClaims(refreshToken);
            String email = claims.getSubject();
            String saveRefreshStorage = refreshStorage.get(email);
            if(saveRefreshStorage != null && saveRefreshStorage.equals(refreshToken)) {
                User user = userService.getByEmail(email)
                        .orElseThrow(() -> new AuthException("User with name " + email + " not found"));
                String accessToken = jwtService.generateAccessToken(user);
                String newRefreshToken = jwtService.generateRefreshToken(user);
                refreshStorage.put(user.getEmail(), newRefreshToken);
                return new JwtResponseDto(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Invalid refresh token");
    }
}
