package com.vorova.todo.dao.abstracts;

import com.vorova.todo.models.entity.Role;

import java.util.Optional;

public interface RoleDao {

    void addRole(Role role);

    Optional<Role> getRoleByAuthority(String authority);

}
