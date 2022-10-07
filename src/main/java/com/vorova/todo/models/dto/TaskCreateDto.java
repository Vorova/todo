package com.vorova.todo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Schema(name = "Task DTO")
@Data
public class TaskCreateDto {
    @Schema(title = "title task")
    private String title;
    @Schema(title = "description for task")
    private String description;
    @Schema(title = "boolean is repeat?")
    private boolean isRepeat;
    @Schema(title = "deadline for the task")
    private Date dateDeadline;
}
