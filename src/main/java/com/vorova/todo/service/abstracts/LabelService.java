package com.vorova.todo.service.abstracts;

import com.vorova.todo.models.entity.Label;

import java.util.List;

public interface LabelService {

    Label add(Label label);

    List<Label> getAllLabelsByUserId(long id);

    Label update(Label label);
}
