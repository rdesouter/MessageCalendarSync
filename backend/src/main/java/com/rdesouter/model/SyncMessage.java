package com.rdesouter.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "message", schema = "public")
public class SyncMessage {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<PartMessage> parts;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "calendar_id")
    private SyncEvent syncEvent;

    public SyncMessage() {
    }


    public SyncMessage(List<PartMessage> parts, SyncEvent syncEvent) {
        this.parts = parts;
        this.syncEvent = syncEvent;
    }

    public int getId() {
        return id;
    }
    public List<PartMessage> getParts() {
        return parts;
    }
    public SyncEvent getCalendarEvent() {
        return syncEvent;
    }
}
