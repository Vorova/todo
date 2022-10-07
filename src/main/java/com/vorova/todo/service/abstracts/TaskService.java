package com.vorova.todo.service.abstracts;

import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.entity.Task;

public interface TaskService {

    void createTask(Task task) throws CheckRequestException;

}
