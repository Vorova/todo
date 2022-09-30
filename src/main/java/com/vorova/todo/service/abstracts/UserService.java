package com.vorova.todo.service.abstracts;

import com.vorova.todo.models.entity.User;

import java.util.Optional;

public interface UserService {

    void addUser(User user);

    Optional<User> getByLogin(String login);

}
