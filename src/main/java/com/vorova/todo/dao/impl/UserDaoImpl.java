package com.vorova.todo.dao.impl;

import com.vorova.todo.dao.abstracts.UserDao;
import com.vorova.todo.models.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Component
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return Optional.of(entityManager.createQuery("""
                SELECT u FROM User u JOIN FETCH u.authorities WHERE u.username = :username OR u.email = :username
                """, User.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
