package com.rdesouter.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "calendar")
public class CalendarEvent {

    @Id
    @Column(length = 36)
    private String id;
    private String subject;
    private String description;
    private String address;
    @Column(name = "beginAt", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime begin;
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime finish;
    @ElementCollection
    @CollectionTable(name = "attendees")
    private List<String> attendees;

    public CalendarEvent() {
    }

    public CalendarEvent(String id, String subject) {
        this.id = id;
        this.subject = subject;
    }

    public CalendarEvent(String id, String subject, String description, String address, LocalDateTime begin, LocalDateTime finish, List<String> attendees) {
        this.id = id;
        this.subject = subject;
        this.description = description;
        this.address = address;
        this.begin = begin;
        this.finish = finish;
        this.attendees = attendees;
    }

    public String getId() {
        return id;
    }
    public String getSubject() {
        return subject;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
}
