package com.vorova.todo.service.abstracts;

import com.vorova.todo.models.dto.JwtRequestDto;
import com.vorova.todo.models.dto.JwtResponseDto;
import com.vorova.todo.security.JwtAuthentication;

import javax.security.auth.message.AuthException;

public interface AuthService {

    JwtResponseDto login(JwtRequestDto requestDto) throws AuthException;

    JwtResponseDto getAccessToken(String refreshToken) throws Exception;

    JwtResponseDto refresh(String refreshToken) throws Exception;

    JwtAuthentication getAuthInfo();

}
