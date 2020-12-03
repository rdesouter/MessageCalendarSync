package com.rdesouter.model;

// authenticated user
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticatedUser {

    public Long id;
    public String name;
    public String email;
    public String refreshToken;

    public AuthenticatedUser(Long id, String name, String email, String refreshToken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
}
