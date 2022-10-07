package com.vorova.todo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(name = "DTO Task")
@Data
@AllArgsConstructor
public class TaskDto {
    @Schema(title = "title task")
    private String title;
    @Schema(title = "description for task")
    private String description;
    @Schema(title = "boolean is repeat?")
    private boolean isRepeat;
    @Schema(title = "deadline for the task")
    private LocalDateTime dateDeadline;
}
