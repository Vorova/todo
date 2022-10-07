package com.vorova.todo.dao.impl;

import com.vorova.todo.dao.abstracts.TaskDao;
import com.vorova.todo.models.entity.Task;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public class TaskDaoImpl implements TaskDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void createTask(Task task) {
        entityManager.persist(task);
    }

    @Override
    public Optional<Task> getTaskById(long idTask) {
        try {
            return Optional.of(entityManager.createQuery("""
                    SELECT t FROM Task t WHERE t.id = :idTask
                """, Task.class)
                    .setParameter("idTask", idTask)
                    .getSingleResult());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}