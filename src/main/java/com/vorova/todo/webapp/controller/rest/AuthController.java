package com.vorova.todo.webapp.controller.rest;

import com.vorova.todo.models.dto.JwtRequestDto;
import com.vorova.todo.models.dto.JwtResponseDto;
import com.vorova.todo.models.dto.RefreshJwtRequestDto;
import com.vorova.todo.service.abstracts.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;

@Tag(name = "Контроллер авторизации", description = "Выдача JWT")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Авторизация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "205", description = "Успешная авторизация, JWT выданы"),
            @ApiResponse(responseCode = "401", description = "Авторизация не удалась"),
            @ApiResponse(responseCode = "403", description = "Пользователь уже авторизован")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequestDto requestDto) {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            JwtResponseDto jwtResponseDto = authService.login(requestDto);
            return ResponseEntity.ok(jwtResponseDto);
        } catch (AuthException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Получение нового Access токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно возвращён Access токен"),
            @ApiResponse(responseCode = "401", description = "Не удалось обновить Access токен")
    })
    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody RefreshJwtRequestDto refreshJwtRequestDto) {
        try {
            JwtResponseDto jwtResponseDto = authService.getAccessToken(refreshJwtRequestDto.getRefreshToken());
            return ResponseEntity.ok(jwtResponseDto);
        } catch (AuthException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Обновление Access и Refresh токенов")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешно возвращены Access и Refresh токен"),
        @ApiResponse(responseCode = "401", description = "Не удалось обновить токены")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshJwtRequestDto refreshJwtRequestDto) {
        try {
            JwtResponseDto jwtResponseDto = authService.refresh(refreshJwtRequestDto.getRefreshToken());
            return ResponseEntity.ok(jwtResponseDto);
        } catch (AuthException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
