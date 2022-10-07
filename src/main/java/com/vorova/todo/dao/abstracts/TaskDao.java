package com.vorova.todo.dao.abstracts;

import com.vorova.todo.models.entity.Task;

import java.util.Optional;

public interface TaskDao {

    void createTask(Task task);

    Optional<Task> getTaskById(long id);


}