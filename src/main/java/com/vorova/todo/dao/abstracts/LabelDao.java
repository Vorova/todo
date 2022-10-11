package com.vorova.todo.dao.abstracts;

import com.vorova.todo.models.entity.Label;

import java.util.List;
import java.util.Optional;

public interface LabelDao {

    Label persist(Label label);

    List<Label> getAllLabelsByUserId(long userId);

    Optional<Label> getLastLabel(long userId);

    Optional<Label> getLabelById(long labelId);

    Label update(Label label);
}
