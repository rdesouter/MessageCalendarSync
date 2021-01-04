package com.rdesouter.model;

import javax.persistence.*;

@Entity
@Table(name = "message", schema = "public")
public class SyncMessage {

    @Id
    @GeneratedValue
    private int id;
    @Column(columnDefinition="TEXT")
    private String payload;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "calendar_id")
    private SyncEvent syncEvent;

    public SyncMessage() {
    }

    public SyncMessage(String payload) {
        this.payload = payload;
    }

    public SyncMessage(String payload, SyncEvent syncEvent) {
        this.payload = payload;
        this.syncEvent = syncEvent;
    }

    public int getId() {
        return id;
    }
    public String getPayload() {
        return payload;
    }
    public SyncEvent getCalendarEvent() {
        return syncEvent;
    }
}
