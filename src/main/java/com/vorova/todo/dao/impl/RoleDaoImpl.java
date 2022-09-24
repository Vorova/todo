package com.vorova.todo.dao.impl;

import com.vorova.todo.dao.abstracts.RoleDao;
import com.vorova.todo.models.entity.Role;
import com.vorova.todo.models.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public Role getRoleByAuthority(String authority) {
        return entityManager.createQuery("""
            SELECT r FROM Role r WHERE r.authority = :authority
        """, Role.class)
                .setParameter("authority", Roles.ROLE_USER)
                .getSingleResult();
    }

}
