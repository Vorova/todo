package com.vorova.todo.webapp.controllers.rest;

import com.vorova.todo.models.entity.User;
import com.vorova.todo.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/general")
public class GeneralResourceController {

    private final UserService userService;

    @Autowired
    public GeneralResourceController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add_user")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        // todo осуществить проверку данных для пользователя;
        // todo роли по умолчанию
        userService.addUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
