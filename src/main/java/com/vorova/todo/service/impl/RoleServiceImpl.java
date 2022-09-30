package com.vorova.todo.service.impl;

import com.vorova.todo.dao.abstracts.RoleDao;
import com.vorova.todo.models.entity.Role;
import com.vorova.todo.service.abstracts.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public void addRole(Role role) {
         roleDao.addRole(role);
    }

    public Role getRoleByAuthority(String authority) {
        Optional<Role> role = roleDao.getRoleByAuthority(authority);
        return role.orElse(null);
    }

}
