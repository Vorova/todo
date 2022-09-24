package com.vorova.todo.dao.abstracts;

import com.vorova.todo.models.entity.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> findByUsername(String username);

    void addUser(User user);

}
