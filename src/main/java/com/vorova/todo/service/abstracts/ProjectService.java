package com.vorova.todo.service.abstracts;

import com.vorova.todo.models.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    Project add(Project project);

    Project update(Project project);

    List<Project> getProjectsByUserId(long userId);

    Optional<Project> getProjectById (long projectId);

}
