package com.vorova.todo.service.impl;

import com.vorova.todo.dao.abstracts.ProjectDao;
import com.vorova.todo.models.entity.Project;
import com.vorova.todo.service.abstracts.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDao projectDao;
    @Autowired
    public ProjectServiceImpl(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    @Override
    @Transactional
    public Project add(Project project) {
        Optional<Project> lastProject = projectDao.getLastProjectOfUserByUserId(project.getAuthor().getId());
        Project persistedProject = projectDao.persist(project);

        // reordered
        if(lastProject.isPresent()) {
            Project lastProjectGet = lastProject.get();
            lastProjectGet.setNextProjectId(persistedProject.getId());
            projectDao.update(lastProjectGet);
        } else {
            persistedProject.setFirst(true);
            projectDao.update(project);
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
