package com.vorova.todo.models.dto;

import com.vorova.todo.models.entity.Label;
import com.vorova.todo.models.entity.Project;
import com.vorova.todo.models.entity.Section;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "DTO Task")
@Data
@AllArgsConstructor
public class TaskDto {

    @Schema(title = "id task")
    private long id;

    @Schema(title = "id of parent")
    private long parentId;

    @Schema(title = "marker - task done")
    private boolean isDone;

    @Schema(title = "title task")
    private String title;

    @Schema(title = "description for task")
    private String description;

    @Schema(name = "date persist task")
    private LocalDateTime datePersist;

    @Schema(name = "date last change task")
    private LocalDateTime dateLastChange;

    @Schema(title = "deadline for the task")
    private LocalDateTime dateDeadline;

    @Schema(title = "boolean is repeat?")
    private String repeat;

    @Schema(title = "mark for first task in the section")
    private boolean isFirst;

    @Schema(name = "id next task in the section")
    private long idNextTask;

    @Schema(name = "list labels")
    private List<Label> labels;

    @Schema(name = "project")
    private Project project;

    @Schema(name = "section")
    private Section section;
}
