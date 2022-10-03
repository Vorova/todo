package com.vorova.todo.exception;

import com.vorova.todo.exception.error.ErrorUserReg;
import lombok.Getter;

import java.util.List;

@Getter
public class UserRegException extends Exception {

    private List<ErrorUserReg> errors;

    public UserRegException(String message) {
        super(message);
    }

    public UserRegException(List<ErrorUserReg> errors) {
        this.errors = errors;
    }
}