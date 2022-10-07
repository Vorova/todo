package com.vorova.todo.service;

import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.entity.Role;
import com.vorova.todo.models.entity.User;
import com.vorova.todo.service.abstracts.RoleService;
import com.vorova.todo.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public ApplicationRunnerImpl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void run(ApplicationArguments args) throws CheckRequestException {
        addRoles();
        addUsers(5);
    }

    private void addRoles() {

        Role admin = new Role();
        admin.setAuthority("ROLE_ADMIN");

        Role user = new Role();
        user.setAuthority("ROLE_USER");

        List<Role> roles = new ArrayList<>();
        roles.add(admin);
        roles.add(user);

        for(Role role: roles) {
            roleService.addRole(role);
        }
    }

    private void addUsers(int count) throws CheckRequestException {

        for (int i = 1; i <= count; i++){
            User user = new User();

            user.setEmail("email" + i + "@mail.ru");
            user.setPassword("password" + i);

            try {
                userService.addUser(user);
            } catch (CheckRequestException ex) {
                throw new CheckRequestException(ex.getMessage());
            }
        }
    }

}
