package com.vorova.todo.service.abstracts;

import com.vorova.todo.models.entity.Role;


public interface RoleService {

    void addRole(Role role);

    Role getRoleByAuthority(String authority);

}
