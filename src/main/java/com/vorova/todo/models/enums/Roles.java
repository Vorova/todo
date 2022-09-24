package com.vorova.todo.models.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Roles {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String authority;

    @Override
    public String toString() {
        return authority;
    }

}
