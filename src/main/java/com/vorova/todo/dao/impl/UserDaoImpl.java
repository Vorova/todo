package com.vorova.todo.dao.impl;

import com.vorova.todo.dao.abstracts.UserDao;
import com.vorova.todo.models.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Component
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public User add(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }

    @Override
    public User update(User user) {
        entityManager.merge(user);
        entityManager.flush();
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return Optional.of(entityManager.createQuery("""
                SELECT u FROM User u JOIN FETCH u.authorities WHERE u.email = :username
                """, User.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getById(long userId) {
        try {
            return Optional.of(entityManager.createQuery("""
                SELECT u FROM User u WHERE u.id = :userId
                """, User.class)
                    .setParameter("userId", userId)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("""
                    SELECT u FROM User u
                """, User.class)
                .getResultList();
    }
}
