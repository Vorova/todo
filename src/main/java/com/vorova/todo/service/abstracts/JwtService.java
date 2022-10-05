package com.vorova.todo.service.abstracts;

import com.vorova.todo.models.entity.User;
import com.vorova.todo.security.JwtAuthentication;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    boolean validateAccessToken(String accessToken);

    boolean validateRefreshToken(String refreshToken);

    Claims getAccessClaims(String accessToken);

    Claims getRefreshClaims(String refreshToken);

    JwtAuthentication generateAuthentication(Claims claims);

}
