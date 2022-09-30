package com.vorova.todo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "DTO Jwt Refresh")
@Getter
@Setter
public class RefreshJwtRequestDto {

    @Schema(title = "Refresh Token")
    private String refreshToken;
}
