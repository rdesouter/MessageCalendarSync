package com.rdesouter.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsernamePassword {

    public String username;
    public String password;

    public UsernamePassword(
            @JsonProperty(value = "username") String username,
            @JsonProperty(value = "password") String password
    ) {
        this.username = username;
        this.password = password;
    }
}
