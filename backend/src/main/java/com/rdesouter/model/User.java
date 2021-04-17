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
//    @OneToMany(cascade = {CascadeType.ALL})
//    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_user_id"))//TODO issue with fk null in db
//    private List<SyncMessage> syncMessages;

//    @OneToMany(cascade = {CascadeType.ALL})
//    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_user_id"))
//    @OneToMany(cascade = {CascadeType.ALL})
//    private List<SyncEvent> events;

    public User() {
    }


    public User(short id, String login, String password, String role, String token, List<SyncMessage> syncMessages, List<SyncEvent> syncEvents) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.token = token;

    }

    public User(short id, String login, String password, String token) {
        this.id = id;
        this.login = login;
        this.password = password;
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
    public String getToken() {
        return token;
    }
}
