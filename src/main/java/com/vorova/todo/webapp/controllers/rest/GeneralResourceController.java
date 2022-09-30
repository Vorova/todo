package com.vorova.todo.webapp.controllers.rest;

import com.vorova.todo.exception.UserRegException;
import com.vorova.todo.models.dto.UserRegDto;
import com.vorova.todo.service.abstracts.UserService;
import com.vorova.todo.webapp.converters.UserConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Общий контроллер", description = "Для общих действий, без авторизации")
@RestController
@RequestMapping("/api/general")
public class GeneralResourceController {

    private final UserService userService;
    private final UserConverter userConverter;

    @Autowired
    public GeneralResourceController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невозможно зарегистрировать пользователя (Не корректные данные)")
    })
    @PostMapping("/add_user")
    public ResponseEntity<?> addUser(@RequestBody UserRegDto userReg) {
        try {
            userService.addUser(userConverter.userRegDtoToUser(userReg));
        } catch (UserRegException exc) {
            return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
