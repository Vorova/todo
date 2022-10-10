package com.vorova.todo.dao.abstracts;

import com.vorova.todo.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findByUsername(String username);
    Optional<User> getById(long userId);

    User add(User user);

    User update(User user);

    List<User> getAllUsers();



}
