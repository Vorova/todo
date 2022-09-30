package com.vorova.todo.webapp.controllers.rest;

import com.vorova.todo.models.dto.JwtRequestDto;
import com.vorova.todo.models.dto.JwtResponseDto;
import com.vorova.todo.models.dto.RefreshJwtRequestDto;
import com.vorova.todo.service.abstracts.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequestDto requestDto) throws AuthException {
        // todo проверка на авторизованность , и если да, то развернуть
        JwtResponseDto jwtResponseDto = authService.login(requestDto);
        return ResponseEntity.ok(jwtResponseDto);
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> token(@RequestBody RefreshJwtRequestDto refreshJwtRequestDto) throws Exception {
        JwtResponseDto jwtResponseDto = authService.getAccessToken(refreshJwtRequestDto.getRefreshToken());
        return ResponseEntity.ok(jwtResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refresh(@RequestBody RefreshJwtRequestDto refreshJwtRequestDto) throws Exception {
        JwtResponseDto jwtResponseDto = authService.refresh(refreshJwtRequestDto.getRefreshToken());
        return ResponseEntity.ok(jwtResponseDto);
    }

}
