package com.vorova.todo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "DTO User при регистрации")
@Data
public class UserRegDto {

    @Schema(title = "Email")
    private String email;

    @Schema(title = "Password")
    private String password;

}
