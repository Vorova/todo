package com.vorova.todo.service.impl;

import com.vorova.todo.models.entity.Role;
import com.vorova.todo.models.entity.User;
import com.vorova.todo.security.JwtAuthentication;
import com.vorova.todo.service.abstracts.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey jwtAccessesSecret;
    private final SecretKey jwtRefreshSecret;

    public JwtServiceImpl(
            @Value("${jwt.secret.access}") String jwtAccessesSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret) {
        this.jwtAccessesSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessesSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    @Override
    public String generateAccessesToken(User user) {
        final LocalDateTime now = LocalDateTime.now();
        // todo заменить в продакшене на 5 минут
        final Instant accessesExpirationInstant = now.plusDays(2).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessesExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessesSecret)
                .claim("roles", user.getAuthorities())
                .claim("username", user.getUsername())
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(refreshExpiration)
                .signWith(jwtRefreshSecret)
                .compact();
    }

    @Override
    public boolean validateAccessesToken(String accessToken) {
        return validateToken(accessToken, jwtAccessesSecret);
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    @Override
    public Claims getAccessesClaims(String accessesToken) {
        return getClaims(accessesToken, jwtAccessesSecret);
    }

    @Override
    public Claims getRefreshClaims(String refreshToken) {
        return getClaims(refreshToken, jwtRefreshSecret);
    }

    @Override
    public JwtAuthentication generateAuthentication(Claims claims) {
        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setRoles(getRolesForGenerateAuthentication(claims));
        return jwtAuthentication;
    }

    private Set<Role> getRolesForGenerateAuthentication(Claims claims) {
        // todo посмотреть warning
        List<Role> roleList = claims.get("roles", List.class);
        return new HashSet<>(roleList);
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean validateToken(String token, Key secretKey) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            throw new JwtException("Expired Jwt", expEx);
        } catch (UnsupportedJwtException unsEx) {
            throw new JwtException("Unsupported jwt", unsEx);
        } catch (MalformedJwtException malformedEx) {
            throw new JwtException("Malformed Jwt", malformedEx);
        } catch (Exception ex) {
            throw new JwtException("Invalid Jwt", ex);
        }
    }

}
