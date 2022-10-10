package com.vorova.todo.service.impl;

import com.vorova.todo.dao.abstracts.ProjectDao;
import com.vorova.todo.models.entity.Project;
import com.vorova.todo.models.entity.User;
import com.vorova.todo.service.abstracts.ProjectService;
import com.vorova.todo.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDao projectDao;
    private final UserService userService;

    @Autowired
    public ProjectServiceImpl(ProjectDao projectDao, UserService userService) {
        this.projectDao = projectDao;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Project add(Project project, User user) {
        Project lastProject = user.getIdFirstProject() == 0 ? null : projectDao.getLastProjectOfUserByUserId(user.getId());
        Project persistedProject = projectDao.persist(project);

        // reordered
        if(lastProject == null) {
            user.setIdFirstProject(persistedProject.getId());
            userService.update(user);
        } else {
            lastProject.setNextProjectId(persistedProject.getId());
            projectDao.update(lastProject);
        }
        return persistedProject;
    }

    @Override
    public Project update(Project project) {
        return projectDao.update(project);
    }

    @Override
    public List<Project> getProjectsByUserId(long userId) {
        return projectDao.getProjectsByUserId(userId);
    }

    @Override
    public Optional<Project> getProjectById(long projectId) {
        return projectDao.getProjectById(projectId);
    }
}
