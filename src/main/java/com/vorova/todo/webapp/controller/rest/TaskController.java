package com.vorova.todo.webapp.controller.rest;

import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.dto.PageDto;
import com.vorova.todo.models.dto.TaskDto;
import com.vorova.todo.models.entity.Task;
import com.vorova.todo.service.abstracts.TaskService;
import com.vorova.todo.service.abstracts.UserService;
import com.vorova.todo.service.abstracts.dto.TaskDtoService;
import com.vorova.todo.webapp.converter.TaskConverter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/task")
@Tag(name = "Api task")
public class TaskController {

    private final TaskService taskService;
    private final TaskConverter taskConverter;
    private final UserService userService;
    private final TaskDtoService taskDtoService;

    @Autowired
    public TaskController(TaskService taskService, TaskConverter taskConverter, UserService userService, TaskDtoService taskDtoService) {
        this.taskService = taskService;
        this.taskConverter = taskConverter;
        this.userService = userService;
        this.taskDtoService = taskDtoService;
    }

    @PostMapping("/create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
            @ApiResponse(responseCode = "400", description = "Задача не создана (не корректные данные)")
    })
    public ResponseEntity<?> create(@RequestBody TaskDto taskDto) {
        Task task = taskConverter.taskDtoToTask(taskDto);
        task.setUser(userService.getAuthenticatedUser());
        try {
            taskService.createTask(task);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CheckRequestException ex) {
            return new ResponseEntity<>(ex.getErrors(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Возвращены все задачи пользователя"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
    })
    public ResponseEntity<?> getAllTasksForUser(
            @RequestParam(name = "items", defaultValue = "50", required = false) int items,
            @RequestParam(name = "page") int page) {

        Map<String, Object> params = new HashMap<>();

        params.put("itemsOnPage", items);
        params.put("currentPageNumber", page);
        params.put("userId", userService.getAuthenticatedUser().getId());

        try {
            PageDto<TaskDto> pageDto = taskDtoService.getPageDto("allTasksForUserPagination", params);
            return new ResponseEntity<>(pageDto, HttpStatus.OK);
        } catch (CheckRequestException ex) {
            return new ResponseEntity<>(ex.getErrors(), HttpStatus.BAD_REQUEST);
        }
    }

}
