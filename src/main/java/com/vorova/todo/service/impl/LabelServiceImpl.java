package com.vorova.todo.service.impl;

import com.vorova.todo.dao.abstracts.LabelDao;
import com.vorova.todo.models.entity.Label;
import com.vorova.todo.service.abstracts.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class LabelServiceImpl implements LabelService {

    private final LabelDao labelDao;

    @Autowired
    public LabelServiceImpl(LabelDao labelDao) {
        this.labelDao = labelDao;
    }

    @Override
    @Transactional
    public Label add(Label label) {
        return reorder(label);
    }

    @Override
    public List<Label> getAllLabelsByUserId(long userId) {
        return labelDao.getAllLabelsByUserId(userId);
    }

    private Label reorder(Label label) {
        if (label.getNextId() != 0 && getLabelById(label.getNextId()).isEmpty()) {
            label.setNextId(0);
        }
        Optional<Label> lastLabel = labelDao.getLastLabel(label.getAuthor().getId());
        Label persistedLabel = labelDao.persist(label);

        if(lastLabel.isPresent()) {
            Label lastLabelGet = lastLabel.get();
            lastLabelGet.setNextId(persistedLabel.getId());
            update(lastLabelGet);
        } else {
            persistedLabel.setFirst(true);
            update(persistedLabel);
        }

        return persistedLabel;
    }

    @Override
    public Label update(Label label) {
        return labelDao.update(label);
    }

    private Optional<Label> getLabelById(long labelId) {
        return labelDao.getLabelById(labelId);
    }

}
