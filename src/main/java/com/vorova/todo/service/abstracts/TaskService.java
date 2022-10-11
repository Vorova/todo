package com.vorova.todo.service.abstracts;

import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.entity.Task;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task add(Task task) throws CheckRequestException;

    Optional<Task> getTaskById(long taskId);

    @Transactional
    Task update(Task task);

    List<Task> getAllTasksByUserId(long id);
}
