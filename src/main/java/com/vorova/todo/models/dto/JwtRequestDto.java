package com.vorova.todo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Jwt Request")
@Getter
@Setter
public class JwtRequestDto {

    @Schema(title = "email")
    private String email;
    @Schema(title = "password")
    private String password;

}
