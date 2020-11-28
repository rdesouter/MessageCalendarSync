package com.rdesouter.model;

import java.util.List;

public class UserWithRoles {

    public Long id;
    public String name;
    public List<Role> roles;
    public String password;

    public UserWithRoles(Long id, String name, List<Role> roles, String password) {
        this.id = id;
        this.name = name;
        this.roles = roles;
        this.password = password;
    }
}
