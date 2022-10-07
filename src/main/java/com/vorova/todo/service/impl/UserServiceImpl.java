package com.vorova.todo.service.impl;

import com.vorova.todo.dao.abstracts.UserDao;
import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.dto.TypeErrorDto;
import com.vorova.todo.models.entity.Role;
import com.vorova.todo.models.entity.User;
import com.vorova.todo.service.abstracts.RoleService;
import com.vorova.todo.service.abstracts.UserService;
import com.vorova.todo.service.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserUtil userUtil;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, RoleService roleService, UserUtil userUtil) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userUtil = userUtil;
    }

    @Transactional
    public void addUser(User user) throws CheckRequestException {

        List<TypeErrorDto> errors = new ArrayList<>();

        if (getByEmail(user.getEmail()).isPresent()) {
            errors.add(new TypeErrorDto("Not unique Email", 1));
        }
        if (!userUtil.isCorrectEmail(user.getEmail())) {
            errors.add(new TypeErrorDto("In correct email", 2));
        }
        if (!userUtil.isCorrectPassword(user.getPassword())) {
            errors.add(new TypeErrorDto("Unreliable password", 3));
        }

        if (!errors.isEmpty()) {
            throw new CheckRequestException(errors);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDatePersist(LocalDateTime.now());

        if(user.getAuthorities() == null) {
            List<Role> roles = new ArrayList<>();
            roles.add(roleService.getRoleByAuthority("ROLE_USER"));
            user.setAuthorities(roles);
        }

        userDao.addUser(user);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userDao.findByUsername(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByUsername(email);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("User with name " + email + " not found");
        }
    }

    @Override
    public User getAuthenticatedUser() {
        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> user = getByEmail(email);
            if (user.isPresent()) {
                return user.get();
            } else {
                throw new RuntimeException("Authenticated user isn't authenticated");
            }
        } else {
            throw new RuntimeException("Attempt to get user, but user isn't authenticated");
        }
    }
}