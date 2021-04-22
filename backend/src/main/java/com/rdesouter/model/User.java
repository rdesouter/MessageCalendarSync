package com.rdesouter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;
    private String login;
//    @JsonIgnore
    private String password;
    private String role;
    private String token;

    public User() {
    }

    public User(short id, String login, String password, String role, String token) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.token = token;
    }

    public short getId() {
        return id;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }
    public String getToken() {
        return token;
    }
}
