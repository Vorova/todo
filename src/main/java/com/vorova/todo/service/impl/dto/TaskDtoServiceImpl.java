package com.vorova.todo.service.impl.dto;

import com.vorova.todo.dao.abstracts.dto.PageDtoDao;
import com.vorova.todo.models.dto.TaskDto;
import com.vorova.todo.service.abstracts.dto.TaskDtoService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TaskDtoServiceImpl extends PageDtoServiceImpl<TaskDto> implements TaskDtoService {
    public TaskDtoServiceImpl(Map<String, PageDtoDao<TaskDto>> stringPageDtoDaoMap) {
        super(stringPageDtoDaoMap);
    }
}
