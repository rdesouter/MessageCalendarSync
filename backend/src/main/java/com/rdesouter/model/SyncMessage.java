package com.rdesouter.model;

import javax.persistence.*;

@Entity
@Table(name = "message", schema = "public")
public class SyncMessage {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "message_part_id")
    private MessagePortion part;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "calendar_id")
    private SyncEvent syncEvent;

    public SyncMessage() {
    }


    public SyncMessage(MessagePortion parts, SyncEvent syncEvent) {
        this.part = parts;
        this.syncEvent = syncEvent;
    }

    public int getId() {
        return id;
    }
    public MessagePortion getPart() {
        return part;
    }
    public SyncEvent getCalendarEvent() {
        return syncEvent;
    }
}
