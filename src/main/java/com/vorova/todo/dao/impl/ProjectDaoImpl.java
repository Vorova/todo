package com.vorova.todo.dao.impl;

import com.vorova.todo.dao.abstracts.ProjectDao;
import com.vorova.todo.models.entity.Project;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class ProjectDaoImpl implements ProjectDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Project persist(Project project) {
        entityManager.persist(project);
        entityManager.flush();
        return project;
    }

    @Override
    public Optional<Project> getLastProjectOfUserByUserId(long userId) {
        try {
            return Optional.of(entityManager.createQuery("""
                SELECT p FROM Project p WHERE p.author.id = :userId AND p.nextProjectId = 0
            """, Project.class)
                    .setParameter("userId", userId)
                    .getSingleResult());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public Project update(Project project) {
        entityManager.merge(project);
        entityManager.flush();
        return project;
    }

    @Override
    public List<Project> getProjectsByUserId(long userId) {
        return entityManager.createQuery("""
            SELECT p FROM Project p WHERE p.author.id = :userId
            """, Project.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public Optional<Project> getProjectById(long projectId) {
        try {
            return Optional.of(entityManager.createQuery("""
                SELECT p FROM Project p WHERE p.id = :projectId
                """, Project.class)
                    .setParameter("projectId", projectId)
                    .getSingleResult());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
