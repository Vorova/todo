package com.vorova.todo.models.dto;

import lombok.Data;

@Data
public class JwtRequestDto {

    private String login;
    private String password;

}
