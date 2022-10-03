package com.vorova.todo.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorUserReg {

    private String message;
    private int type;

}
