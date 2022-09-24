package com.vorova.todo.service;

import com.vorova.todo.models.entity.Role;
import com.vorova.todo.models.entity.User;
import com.vorova.todo.models.enums.Roles;
import com.vorova.todo.service.abstracts.RoleService;
import com.vorova.todo.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    private final UserService userService;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationRunnerImpl(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    private void addRoles() {

        Role admin = new Role();
        admin.setAuthority(Roles.ROLE_ADMIN);

        Role user = new Role();
        user.setAuthority(Roles.ROLE_USER);

        List<Role> roles = new ArrayList<>();
        roles.add(admin);
        roles.add(user);

        for(Role role: roles) {
            roleService.addRole(role);
        }
    }

    private void addUsers(int count) {

        List<Role> roles = new ArrayList<>();
        roles.add(roleService.getRoleByAuthority("ROLE_USER"));

        for (int i = 1; i <= count; i++){
            User user = new User();

            user.setUsername("name" + i);
            user.setEmail(i + "@mail.ru");
            user.setPassword(passwordEncoder.encode("pass" + i));
            user.setDatePersist(Date.from(Instant.now()));
            user.setRoles(roles);

            userService.addUser(user);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        addRoles();
        addUsers(25);
    }
}
