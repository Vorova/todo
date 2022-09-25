package com.vorova.todo.service.abstracts;

import com.vorova.todo.models.entity.User;

public interface UserService {

    void addUser(User user);

    User getUserByName(String name);

}
