package com.rdesouter.model;

// authenticated user
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "user", schema = "public")
public class AuthenticatedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;
    private String name;
    private String email;
    private String refreshToken;
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_user_id"))
    private List<Message> messages;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_user_id"))
    private List<CalendarEvent> calendars;

    public AuthenticatedUser(String name, String email, String refreshToken, List<Message> messages, List<CalendarEvent> calendars) {
        this.name = name;
        this.email = email;
        this.refreshToken = refreshToken;
        this.messages = messages;
        this.calendars = calendars;
    }

    public AuthenticatedUser(short id, String name, String email, String refreshToken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.refreshToken = refreshToken;
    }



    public short getId() {
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
