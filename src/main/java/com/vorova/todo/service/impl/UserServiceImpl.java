package com.vorova.todo.service.impl;

import com.vorova.todo.dao.abstracts.UserDao;
import com.vorova.todo.models.entity.Role;
import com.vorova.todo.models.entity.User;
import com.vorova.todo.service.abstracts.RoleService;
import com.vorova.todo.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Transactional
    public void addUser(User user) {
        // Todo осуществить проверку данных пользователя
        // todo осуществить проверку данных пользователя
        /*
         * Осуществить проверку email на корректность и уникальность.
         * Проверить nickname на уникальность и корректность.
         * Проверить пароль на надёжность.
         */
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDatePersist(Date.from(Instant.now()));

        if(user.getRoles() == null) {
            List<Role> roles = new ArrayList<>();
            roles.add(roleService.getRoleByAuthority("ROLE_USER"));
            user.setRoles(roles);
        }

        userDao.addUser(user);
    }

    @Override
    public Optional<User> getByLogin(String login) {
        return userDao.findByUsername(login);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByUsername(username);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("User with name " + username + " not found");
        }
    }
}