package com.rdesouter.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsernamePassword {

    public String name;
    public String password;

    public UsernamePassword(
            @JsonProperty(value = "name") String name,
            @JsonProperty(value = "password") String password
    ) {
        this.name = name;
        this.password = password;
    }
}
