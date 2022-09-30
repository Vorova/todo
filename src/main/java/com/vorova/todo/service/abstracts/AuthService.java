package com.vorova.todo.service.abstracts;

import com.vorova.todo.models.dto.JwtRequestDto;
import com.vorova.todo.models.dto.JwtResponseDto;

import javax.security.auth.message.AuthException;

public interface AuthService {

    JwtResponseDto login(JwtRequestDto requestDto) throws AuthException;

    JwtResponseDto getAccessToken(String refreshToken) throws AuthException;

    JwtResponseDto refresh(String refreshToken) throws AuthException;

}
