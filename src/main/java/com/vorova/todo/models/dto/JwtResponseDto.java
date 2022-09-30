package com.vorova.todo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "DTO JWT Response")
@Getter
@AllArgsConstructor
public class JwtResponseDto {

    @Schema(title = "Тип токена")
    private final String type = "Bearer";
    @Schema(title = "Access токен")
    private String accessToken;
    @Schema(title = "Refresh токен")
    private String refreshToken;

}