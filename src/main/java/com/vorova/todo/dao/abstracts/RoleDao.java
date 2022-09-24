package com.vorova.todo.dao.abstracts;

import com.vorova.todo.models.entity.Role;

public interface RoleDao {

    void addRole(Role role);

    Role getRoleByAuthority(String authority);

}
