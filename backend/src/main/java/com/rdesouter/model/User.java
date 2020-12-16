package com.rdesouter.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;
    private String login;
    private String password;
    private String role;
    private String token;
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_user_id"))
    private List<Message> messages;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_user_id"))
    private List<CalendarEvent> calendars;

    public User() {
    }

    public User(String login, String password, String role, String token, List<Message> messages, List<CalendarEvent> calendars) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.token = token;
        this.messages = messages;
        this.calendars = calendars;
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
