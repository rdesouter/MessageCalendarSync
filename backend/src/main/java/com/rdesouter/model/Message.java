package com.rdesouter.model;

import javax.persistence.*;

@Entity
@Table(name = "message", schema = "public")
public class Message {

    @Id
    @GeneratedValue
    private int id;
    @Column(columnDefinition="TEXT")
    private String payload;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "calendar_id")
    private CalendarEvent calendarEvent;

    public Message() {
    }

    public Message(String payload, CalendarEvent calendarEvent) {
        this.payload = payload;
        this.calendarEvent = calendarEvent;
    }

    public int getId() {
        return id;
    }
    public String getPayload() {
        return payload;
    }
    public CalendarEvent getCalendarEvent() {
        return calendarEvent;
    }
}
