package com.vorova.todo.dao.impl;

import com.vorova.todo.dao.abstracts.TaskDao;
import com.vorova.todo.models.entity.Project;
import com.vorova.todo.models.entity.Section;
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
    public Task persist(Task task) {
        entityManager.persist(task);
        entityManager.flush();
        return task;
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

    @Override
    public Task update(Task task) {
        entityManager.merge(task);
        entityManager.flush();
        return task;
    }

    @Override
    public Optional<Task> getLastTask(Project project, Section section, long userId) {
        String projectId = project == null ? "IS NULL" : "= " + project.getId();
        String sectionId = section == null ? "IS NULL" : "= " + section.getId();

        String hql = """
            SELECT t FROM Task t
            WHERE t.project.id %s
            AND t.section.id %s
            AND t.user.id = %s
            AND t.idNextTask = 0
            """.formatted(projectId, sectionId, userId);
        try {
            return Optional.of(entityManager.createQuery(hql, Task.class)
                    .getSingleResult());
        } catch (Exception ignored) {
            return Optional.empty();
        }

    }
}