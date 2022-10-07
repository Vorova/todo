package com.vorova.todo.service.abstracts;

import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.entity.User;

import java.util.Optional;

public interface UserService {

    void addUser(User user) throws CheckRequestException;

    Optional<User> getByEmail(String email);

    User getAuthenticatedUser();

}
