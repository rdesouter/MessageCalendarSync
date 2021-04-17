package com.rdesouter.model;

import javax.persistence.*;

@Entity
@Table(name = "message", schema = "public")
public class SyncMessage {

    @Id
    private String id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "message_part_id")
    private MessagePortion portion;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "event_id")
    private SyncEvent syncEvent;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_id"))
    private User user;

    public SyncMessage() {
    }

    public SyncMessage(String id, MessagePortion portion, SyncEvent syncEvent, User user) {
        this.id = id;
        this.portion = portion;
        this.syncEvent = syncEvent;
        this.user = user;
    }

    public String getId() {
        return id;
    }
    public MessagePortion getPortion() {
        return portion;
    }
    public SyncEvent getSyncEvent() {
        return syncEvent;
    }
    public User getUser() {
        return user;
    }
}
