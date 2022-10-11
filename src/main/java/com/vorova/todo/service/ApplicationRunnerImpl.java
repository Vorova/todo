package com.vorova.todo.service;

import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.entity.Project;
import com.vorova.todo.models.entity.Role;
import com.vorova.todo.models.entity.Section;
import com.vorova.todo.models.entity.Task;
import com.vorova.todo.models.entity.User;
import com.vorova.todo.service.abstracts.ProjectService;
import com.vorova.todo.service.abstracts.RoleService;
import com.vorova.todo.service.abstracts.SectionService;
import com.vorova.todo.service.abstracts.TaskService;
import com.vorova.todo.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    private final int COUNT_USERS = 5;
    private final int PERCENTAGE_PROJECTS_AVAILABLE = 70;
    private final int PERCENTAGE_SECTIONS_WITHOUT_PROJECT = 20;
    private final int MAX_COUNT_PROJECTS_FOR_USER = 5;
    private final int MAX_COUNT_SECTIONS_FOR_PROJECT = 3;

    private final UserService userService;
    private final RoleService roleService;
    private final TaskService taskService;
    private final Environment environment;
    private final ProjectService projectService;
    private final SectionService sectionService;

    @Autowired
    public ApplicationRunnerImpl(UserService userService, RoleService roleService, TaskService taskService, Environment environment, ProjectService projectService, SectionService sectionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.taskService = taskService;
        this.environment = environment;
        this.projectService = projectService;
        this.sectionService = sectionService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!Objects.equals(environment.getProperty("spring.jpa.hibernate.ddl-auto"), "create")) return;
        addRoles();
        addUsers();
        addSections();
        addTasks();
        addLabels();
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

    private void addUsers() {
        for (int i = 1; i <= COUNT_USERS; i++){
            User user = new User();

            user.setEmail("email" + i + "@mail.ru");
            user.setPassword("password" + i);

            User persistedUser;

            try {
                persistedUser = userService.add(user);
                addProjects(persistedUser);
            } catch (CheckRequestException ex) {
                throw new CheckRequestException(ex.getMessage());
            }
        }
    }

    void addProjects (User user) {
        int count = 0;
        if(random(0,100) < PERCENTAGE_PROJECTS_AVAILABLE) {
            count = random(1, MAX_COUNT_PROJECTS_FOR_USER);
        }
        for(int i = 1; i <= count; i++) {
            Project project = new Project();
            project.setTitle("Project #" + i + " for user " + user.getEmail());
            project.setAuthor(user);
            projectService.add(project);
        }
    }

    public void addSections() {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            List<Project> projects = projectService.getProjectsByUserId(user.getId());
            for (Project project : projects) {
                for (int i = 0; i < random(1, MAX_COUNT_SECTIONS_FOR_PROJECT); i++) {
                    Section section = new Section();
                    section.setTitle("Project: " + project.getId() + "; User: " + project.getAuthor().getId());
                    section.setProject(project);
                    section.setAuthor(user);
                    try {
                        sectionService.add(section);
                    } catch (CheckRequestException ex) {
                        throw new CheckRequestException(ex.getErrors());
                    }
                }
            }
            if(random(1, 100) < PERCENTAGE_SECTIONS_WITHOUT_PROJECT) {
                Section section = new Section();
                section.setTitle("Without project!");
                section.setProject(null);
                section.setAuthor(user);
                try {
                    sectionService.add(section);
                } catch (CheckRequestException ex) {
                    throw new CheckRequestException(ex.getErrors());
                }
            }
        }
    }

    private void addTasks(){
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            List<Project> projects = projectService.getProjectsByUserId(user.getId());
            for (Project project : projects) {
                List<Section> sections = sectionService.getSectionsByProjectId(project.getId());
                for (Section section : sections) {
                    for(int i = 0; i < random(1, 5); i++) {
                        Task task = new Task();
                        task.setUser(user);
                        task.setTitle("Task #" + i + " for user: " + user.getEmail());
                        task.setDescription("Some description");
                        task.setProject(random(1, 10) < 3 ? null : project);
                        task.setSection(random(1,10) < 3 || task.getProject() == null ? null : section);
                        taskService.add(task);
                    }
                }
            }
        }
    }

    private void addLabels() {
        // todo добавить labels
    }

    int random(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

}
