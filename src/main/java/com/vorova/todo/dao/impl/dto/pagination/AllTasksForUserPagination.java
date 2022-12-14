package com.vorova.todo.dao.impl.dto.pagination;

import com.vorova.todo.dao.abstracts.dto.PageDtoDao;
import com.vorova.todo.models.dto.TaskDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository
public class AllTasksForUserPagination implements PageDtoDao<TaskDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TaskDto> getItems(Map<String, Object> params) {
        return entityManager.createQuery("""
                SELECT new com.vorova.todo.models.dto.TaskDto(
                t.title,
                t.description, 
                t.isDone,
                t.isRepeat, 
                t.repeat,
                t.dateDeadline)
                FROM Task t WHERE t.user.id = :userId
                """, TaskDto.class)
                .setParameter("userId", params.get("userId"))
                .getResultList();
    }

    @Override
    public int getCountItems(Map<String, Object> params) {
        return Math.toIntExact(entityManager.createQuery("""
                            SELECT COUNT(t) FROM Task t WHERE t.user.id = :userId
                            """, Long.class)
            .setParameter("userId", params.get("userId"))
            .getSingleResult());
    }
}
