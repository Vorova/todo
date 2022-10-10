package com.vorova.todo.webapp.controller.rest;

import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.dto.UserRegDto;
import com.vorova.todo.service.abstracts.UserService;
import com.vorova.todo.webapp.converter.UserConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Проверка аутентификации")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Авторизован"),
            @ApiResponse(responseCode = "403", description = "Не авторизован")
    })
    @PostMapping("/check_auth")
    public ResponseEntity<?> checkAuth() {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невозможно зарегистрировать пользователя (Не корректные данные)"),
            @ApiResponse(
                    responseCode = "403",
                    description = "Авторизованный пользователь не может регистрировать нового пользователя"
            )
    })
    @PostMapping("/add_user")
    public ResponseEntity<?> addUser(@RequestBody UserRegDto userReg) {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            userService.add(userConverter.userRegDtoToUser(userReg));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CheckRequestException exc) {
            return new ResponseEntity<>(exc.getErrors(), HttpStatus.BAD_REQUEST);
        }
    }
}
