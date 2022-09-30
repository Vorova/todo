package com.vorova.todo.service.abstracts;

import com.vorova.todo.models.entity.User;
import com.vorova.todo.security.JwtAuthentication;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String generateAccessesToken(User user);

    String generateRefreshToken(User user);

    boolean validateAccessesToken(String accessToken) throws Exception;

    boolean validateRefreshToken(String refreshToken) throws Exception;

    Claims getAccessesClaims(String accessesToken);

    Claims getRefreshClaims(String refreshToken);

    JwtAuthentication generateAuthentication(Claims claims);

}