package com.vorova.todo.service;

import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.entity.Role;
import com.vorova.todo.models.entity.Task;
import com.vorova.todo.models.entity.User;
import com.vorova.todo.service.abstracts.RoleService;
import com.vorova.todo.service.abstracts.TaskService;
import com.vorova.todo.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final TaskService taskService;
    private final Environment environment;

    @Autowired
    public ApplicationRunnerImpl(UserService userService, RoleService roleService, TaskService taskService, Environment environment) {
        this.userService = userService;
        this.roleService = roleService;
        this.taskService = taskService;
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!Objects.equals(environment.getProperty("spring.jpa.hibernate.ddl-auto"), "create")) return;
        addRoles();
        addUsers(15);
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

    private void addUsers(int count) throws Exception {

        for (int i = 1; i <= count; i++){
            User user = new User();

            user.setEmail("email" + i + "@mail.ru");
            user.setPassword("password" + i);

            try {
                userService.addUser(user);
            } catch (CheckRequestException ex) {
                throw new CheckRequestException(ex.getMessage());
            }

            // у 75% пользователей есть задачи
            if(random(0, 100) < 75) {
                // количество задач варьируется от 2-5
                addTasks(user, random(1, 5));
            }
        }
    }

    void addTasks(User user, int count) throws Exception {
        for(int i = 1; i <= count; i++) {
            Task task = new Task();

            task.setTitle("Task " + i + " for " + user.getEmail());
            task.setDescription("some description fot " + i + " task ");
            task.setUser(user);

            // 50% задач имеют deadline
            if (random(0, 100) < 50) {
                LocalDateTime date = LocalDateTime.now().plusDays(random(1, 15)).plusHours(random(0,24));
                task.setDateDeadline(date);
            }

            try {
                taskService.createTask(task);
            } catch (CheckRequestException ex) {
                throw new CheckRequestException(ex.getErrors().toString());
            }
        }
    }

    int random(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

}
