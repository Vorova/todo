package com.vorova.todo.service.impl;

import com.vorova.todo.dao.abstracts.RoleDao;
import com.vorova.todo.models.entity.Role;
import com.vorova.todo.service.abstracts.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // todo Вернуть Optional
    public Role getRoleByAuthority(String authority) {
        return roleDao.getRoleByAuthority(authority);
    }

}
