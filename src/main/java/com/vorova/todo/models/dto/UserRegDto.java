package com.vorova.todo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "DTO User при регистрации")
@Getter
@Setter
public class UserRegDto {

    @Schema(title = "Email")
    private String email;

    @Schema(title = "Password")
    private String password;

}
