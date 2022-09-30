package com.vorova.todo.service.abstracts;

import com.vorova.todo.exception.UserRegException;
import com.vorova.todo.models.entity.User;

import java.util.Optional;

public interface UserService {

    void addUser(User user) throws UserRegException;

    Optional<User> getByEmail(String email);

}
