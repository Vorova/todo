package com.vorova.todo.webapp.controller.rest;

import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.dto.TaskCreateDto;
import com.vorova.todo.models.entity.Task;
import com.vorova.todo.service.abstracts.TaskService;
import com.vorova.todo.service.abstracts.UserService;
import com.vorova.todo.webapp.converter.TaskConverter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/task")
@Tag(name = "Api task")
public class TaskController {

    private final TaskService taskService;
    private final TaskConverter taskConverter;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, TaskConverter taskConverter, UserService userService) {
        this.taskService = taskService;
        this.taskConverter = taskConverter;
        this.userService = userService;
    }

    @PostMapping("/create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
            @ApiResponse(responseCode = "400", description = "Задача не создана (не корректные данные)")
    })
    public ResponseEntity<?> create(@RequestBody TaskCreateDto taskDto) {
        Task task = taskConverter.taskDtoToTask(taskDto);
        task.setUser(userService.getAuthenticatedUser());
        try {
            taskService.createTask(task);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CheckRequestException ex) {
            return new ResponseEntity<>(ex.getErrors(), HttpStatus.BAD_REQUEST);
        }
    }
}
