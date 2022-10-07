package com.vorova.todo.webapp.converter;

import com.vorova.todo.models.dto.TaskDto;
import com.vorova.todo.models.entity.Task;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class TaskConverter {

    public abstract Task taskDtoToTask(TaskDto taskDto);

}
