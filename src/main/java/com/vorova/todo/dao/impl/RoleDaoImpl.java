package com.vorova.todo.dao.impl;

import com.vorova.todo.dao.abstracts.RoleDao;
import com.vorova.todo.models.entity.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void addRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public Optional<Role> getRoleByAuthority(String authority) {
        try {
            return Optional.of(
                    entityManager.createQuery("""
                            SELECT r FROM Role r WHERE r.authority = :authority
                        """, Role.class)
                    .setParameter("authority", authority)
                    .getSingleResult());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

}
