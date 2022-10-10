package com.vorova.todo.service.abstracts;

import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User add(User user) throws CheckRequestException;

    Optional<User> getByEmail(String email);

    Optional<User> getById(long userId);

    User getAuthenticatedUser();

    User update(User user);

    List<User> getAllUsers();

}
