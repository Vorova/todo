package com.vorova.todo.dao.abstracts;

import com.vorova.todo.models.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectDao {

    Project persist(Project project);

    Project getLastProjectOfUserByUserId(long userId);

    Project update(Project project);

    List<Project> getProjectsByUserId(long userId);

    Optional<Project> getProjectById(long projectId);

}
