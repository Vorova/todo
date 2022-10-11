package com.vorova.todo.dao.abstracts;

import com.vorova.todo.models.entity.Project;
import com.vorova.todo.models.entity.Section;
import com.vorova.todo.models.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskDao {

    Task persist(Task task);

    Optional<Task> getTaskById(long id);

    Task update(Task lastTaskGet);

    Optional<Task> getLastTask(Project project, Section section, long userId, long parentId);

    List<Task> getAllTasksByUserId(long userId);
}