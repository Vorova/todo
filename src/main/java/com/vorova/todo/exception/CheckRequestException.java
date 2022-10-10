package com.vorova.todo.exception;

import com.vorova.todo.models.dto.TypeErrorDto;
import lombok.Getter;

import java.util.List;

@Getter
public class CheckRequestException extends RuntimeException {

    private List<TypeErrorDto> errors;

    public CheckRequestException(String message) {
        super(message);
    }

    public CheckRequestException(List<TypeErrorDto> errors) {
        this.errors = errors;
    }
}