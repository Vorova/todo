package com.vorova.todo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(name = "DTO User при регистрации")
@Data
@AllArgsConstructor
public class UserRegDto {

    @Schema(title = "Email")
    private String email;

    @Schema(title = "Password")
    private String password;

}
