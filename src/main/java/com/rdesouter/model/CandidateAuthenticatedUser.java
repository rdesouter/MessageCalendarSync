package com.rdesouter.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

//candidate authenticated user
public class CandidateAuthenticatedUser {

    public Long id;
    public String login;
    public String password;
    public String role;

    public CandidateAuthenticatedUser(Long id, String login, String password, String role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

}
