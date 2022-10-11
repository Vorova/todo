package com.vorova.todo.dao.impl;

import com.vorova.todo.dao.abstracts.SectionDao;
import com.vorova.todo.models.entity.Section;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class SectionDaoImpl implements SectionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Section persist(Section section) {
        entityManager.persist(section);
        entityManager.flush();
        return section;
    }

    @Override
    public Optional<Section> getLastSectionOfProjectByProjectId(long projectId) {
        try {
            return Optional.of(entityManager.createQuery("""
            SELECT s FROM Section s WHERE s.project.id = :projectId AND s.nextIdSection = 0
            """, Section.class)
                    .setParameter("projectId", projectId)
                    .getSingleResult());
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Section update(Section section) {
        entityManager.merge(section);
        entityManager.flush();
        return section;
    }

    @Override
    public List<Section> getSectionByProjectId(long projectId) {
        return entityManager.createQuery("""
                SELECT s FROM Section s WHERE s.project.id = :projectId
                """, Section.class)
                .setParameter("projectId", projectId)
                .getResultList();
    }

    @Override
    public Section getPrevSection(long sectionId) {
        return entityManager.createQuery("""
                SELECT s FROM Section s WHERE s.nextIdSection = :sectionId
                """, Section.class)
                .setParameter("sectionId", sectionId)
                .getSingleResult();
    }

    @Override
    public Optional<Section> getLastSectionInInboxByUserId(long userId) {
        try {
            return Optional.of(entityManager.createQuery("""
                SELECT s FROM Section s WHERE s.nextIdSection = 0 AND s.author.id = :userId
                """, Section.class)
                    .setParameter("userId", userId)
                    .getSingleResult());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Section> getSectionById(long sectionId) {
        try {
            return Optional.of(entityManager.createQuery("""
                        SELECT s FROM Section s WHERE s.id = :sectionId
                        """, Section.class)
                    .setParameter("sectionId", sectionId)
                    .getSingleResult());
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

}
