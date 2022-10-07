package com.vorova.todo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TypeErrorDto {

    private String message;
    private int type;

}
