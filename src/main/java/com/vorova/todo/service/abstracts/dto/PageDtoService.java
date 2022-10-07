package com.vorova.todo.service.abstracts.dto;

import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.dto.PageDto;

import java.util.Map;

public interface PageDtoService<T> {

    PageDto<T> getPageDto(String pageDtoDaoName, Map<String, Object> params) throws CheckRequestException;

}

