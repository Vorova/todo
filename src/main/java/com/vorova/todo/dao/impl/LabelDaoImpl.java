package com.vorova.todo.dao.impl;

import com.vorova.todo.dao.abstracts.LabelDao;
import com.vorova.todo.models.entity.Label;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class LabelDaoImpl implements LabelDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Label persist(Label label) {
        entityManager.persist(label);
        entityManager.flush();
        return label;
    }

    @Override
    public List<Label> getAllLabelsByUserId(long userId) {
        return entityManager.createQuery("""
                SELECT l FROM Label l WHERE l.author.id = :userId
                """, Label.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public Optional<Label> getLastLabel(long userId) {
        try {
            return Optional.of(entityManager.createQuery("""
                    SELECT l FROM Label l WHERE l.author.id = :userId and l.nextId = 0
                    """, Label.class)
                    .setParameter("userId", userId)
                    .getSingleResult());
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Label> getLabelById(long labelId) {
        try {
            return Optional.of(entityManager.createQuery("""
                    SELECT l FROM Label l WHERE l.id = :labelId
                    """, Label.class)
                    .setParameter("labelId", labelId)
                    .getSingleResult());
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Label update(Label label) {
        entityManager.merge(label);
        entityManager.flush();
        return label;
    }
}