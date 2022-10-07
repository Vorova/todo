package com.vorova.todo.service.impl;

import com.vorova.todo.dao.abstracts.TaskDao;
import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.dto.TypeErrorDto;
import com.vorova.todo.models.entity.Task;
import com.vorova.todo.service.abstracts.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskDao taskDao;
    @Autowired
    public TaskServiceImpl(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void createTask(Task task) throws CheckRequestException {
        List<TypeErrorDto> errors = new ArrayList<>();

        if (task.getTitle().isEmpty() || task.getTitle() == null) {
            errors.add(new TypeErrorDto("Title is not be empty", 1));
        }
        if (task.getDateDeadline() != null) {
            if ( task.getDateDeadline().toInstant().compareTo(Instant.now()) <= 0) {
                errors.add(new TypeErrorDto("In correct date deadline", 2));
            }
        }
        if (task.getUser() == null) {
            errors.add(new TypeErrorDto("Not association user for the task", 3));
        }

        if (!errors.isEmpty()) {
            throw new CheckRequestException(errors);
        }

        task.setDatePersist(Date.from(Instant.now()));
        task.setDateLastChange(Date.from(Instant.now()));

        taskDao.createTask(task);
    }
}