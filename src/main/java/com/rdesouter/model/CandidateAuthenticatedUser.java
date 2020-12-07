package com.rdesouter.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

//candidate authenticated user
@Table(name = "user", schema = "public")
@Entity
public class CandidateAuthenticatedUser {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String login;

    @Column
    private String password;

    @Column
    private String role;

    public CandidateAuthenticatedUser() {
    }

    public CandidateAuthenticatedUser(Long id, String login, String password, String role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
