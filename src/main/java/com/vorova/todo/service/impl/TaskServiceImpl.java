package com.vorova.todo.service.impl;

import com.vorova.todo.dao.abstracts.TaskDao;
import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.dto.TypeErrorDto;
import com.vorova.todo.models.entity.Label;
import com.vorova.todo.models.entity.Task;
import com.vorova.todo.service.abstracts.SectionService;
import com.vorova.todo.service.abstracts.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskDao taskDao;
    private final SectionService sectionService;

    @Autowired
    public TaskServiceImpl(TaskDao taskDao, SectionService sectionService) {
        this.taskDao = taskDao;
        this.sectionService = sectionService;
    }

    @Override
    public void add(Task task) throws CheckRequestException {
        List<TypeErrorDto> errors = new ArrayList<>();

        if (task.getTitle().isEmpty() || task.getTitle() == null) {
            errors.add(new TypeErrorDto("Title is not be empty", 1));
        }
        if (task.getDateDeadline() != null) {
            if (task.getDateDeadline().compareTo(LocalDateTime.now()) <= 0) {
                errors.add(new TypeErrorDto("In correct date deadline", 2));
            }
        }
        if (task.getUser() == null) {
            errors.add(new TypeErrorDto("Not association user for the task", 3));
        }
        if (task.getLabels() != null) {
            List<Label> newLabels = new ArrayList<>();
            for (Label label : task.getLabels()) {
                if (label.getAuthor() == task.getUser()) {
                    newLabels.add(label);
                }
            }
            task.setLabels(newLabels);
        }

//        if (!sectionService.isBelongTaskOfSection(task, task.getSection())) {
//            Section inboxSection = sectionService.getInboxSectionInboxProjectByUserId(task.getUser().getId());
//            task.setSection(inboxSection);
//        }

        if (!errors.isEmpty()) {
            throw new CheckRequestException(errors);
        }

        task.setDatePersist(LocalDateTime.now());
        task.setDateLastChange(LocalDateTime.now());

        taskDao.createTask(task);
    }
}