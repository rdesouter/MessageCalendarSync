package com.rdesouter.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "event")
public class SyncEvent {

    @Id
//    @Column(length = 36)
    private String id;
    private String subject;
    private String address;
    @Column(name = "beginDate", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime begin;
    @Column(name = "endDate", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime end;
    @ElementCollection
    @CollectionTable(name = "attendees")
    private List<String> attendees;
    private String description;
    @ElementCollection
    @CollectionTable(name = "keyErrors")
    private List<String> keyErrors;

    public SyncEvent() {
    }

    public SyncEvent(String id) {
        this.id = id;
    }

    public SyncEvent(String id, List<String> keyErrors) {
        this.id = id;
        this.keyErrors = keyErrors;
    }

    public SyncEvent(String id, String subject, String address, LocalDateTime begin, LocalDateTime end, List<String> attendees, String description) {
        this.id = id;
        this.subject = subject;
        this.address = address;
        this.begin = begin;
        this.end = end;
        this.attendees = attendees;
        this.description = description;
    }

    public String getId() {
        return id;
    }
    public String getSubject() {
        return subject;
    }
    public String getDescription() {
        return description;
    }
    public String getAddress() {
        return address;
    }
    public LocalDateTime getBegin() {
        return begin;
    }
    public LocalDateTime getEnd() {
        return end;
    }
    public List<String> getAttendees() {
        return attendees;
    }
    public List<String> getKeyErrors() {
        return keyErrors;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
