package com.vorova.todo.service.impl;

import com.vorova.todo.dao.abstracts.TaskDao;
import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.dto.TypeErrorDto;
import com.vorova.todo.models.entity.Label;
import com.vorova.todo.models.entity.Project;
import com.vorova.todo.models.entity.Section;
import com.vorova.todo.models.entity.Task;
import com.vorova.todo.service.abstracts.ProjectService;
import com.vorova.todo.service.abstracts.SectionService;
import com.vorova.todo.service.abstracts.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskDao taskDao;
    private final SectionService sectionService;
    private final ProjectService projectService;

    @Autowired
    public TaskServiceImpl(TaskDao taskDao, SectionService sectionService, ProjectService projectService) {
        this.taskDao = taskDao;
        this.sectionService = sectionService;
        this.projectService = projectService;
    }

    @Override
    @Transactional
    public Task add(Task task) throws CheckRequestException {
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
        if (!errors.isEmpty()) {
            throw new CheckRequestException(errors);
        }

        // проверка на принадлежность секции к проекту и наоборот
        if (task.getSection() != null) {
            Optional<Section> sectionOptional = sectionService.getSectionById(task.getSection().getId());
            sectionOptional.ifPresentOrElse(task::setSection, null);
        }
        if (task.getProject() != null) {
            Optional<Project> projectOptional = projectService.getProjectById(task.getProject().getId());
            projectOptional.ifPresentOrElse(task::setProject, null);
        }
        if (task.getSection() != null && task.getProject() != null) {
            if(task.getProject().getId() != task.getSection().getProject().getId()) {
                task.setSection(null);
                task.setProject(null);
            }
        }

        task.setDatePersist(LocalDateTime.now());
        task.setDateLastChange(LocalDateTime.now());

        // reorder
        try {
            return reorderAndPersist(task);
        } catch (Exception ex) {
            errors.add(new TypeErrorDto("Failed create task", 4));
            throw new CheckRequestException(errors);
        }
    }

    @Override
    public Optional<Task> getTaskById(long taskId) {
        return taskDao.getTaskById(taskId);
    }

    private Task reorderAndPersist(Task task) {
        if (task.getIdNextTask() != 0 && getTaskById(task.getIdNextTask()).isEmpty()) {
            task.setIdNextTask(0);
        }
        Optional<Task> lastTaskOptional = getLastTask(task.getProject(), task.getSection(), task.getUser().getId());
        Task persistedTask = taskDao.persist(task);

        if(lastTaskOptional.isPresent()) {
            Task lastTaskGet = lastTaskOptional.get();
            lastTaskGet.setIdNextTask(persistedTask.getId());
            update(lastTaskGet);
        } else {
            persistedTask.setFirst(true);
            persistedTask = update(persistedTask);
        }
        return persistedTask;
    }

    @Transactional
    @Override
    public Task update(Task task) {
        return taskDao.update(task);
    }

    private Optional<Task> getLastTask(Project project, Section section, long userId) {
        return taskDao.getLastTask(project, section, userId);
    }

}