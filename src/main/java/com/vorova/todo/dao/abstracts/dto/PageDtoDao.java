package com.vorova.todo.dao.abstracts.dto;

import java.util.List;
import java.util.Map;

public interface PageDtoDao<T> {
    List<T> getItems(Map<String, Object> params);
    int getCountItems(Map<String, Object> params);
}
