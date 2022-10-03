package com.vorova.todo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "DTO User при регистрации")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegDto {

    @Schema(title = "Email")
    private String email;

    @Schema(title = "Password")
    private String password;

}
