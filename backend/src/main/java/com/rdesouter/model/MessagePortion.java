package com.rdesouter.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "message_portion", schema = "public")
public class MessagePortion {
    @Id
    @GeneratedValue
    private final int id;
    @Column(columnDefinition="varchar(50)")
    private final String subject;
    @Column(columnDefinition="varchar(50)")
    private String messageFrom;
    @Column(columnDefinition="varchar(50)")
    private String sendingDate;
    @Column(columnDefinition = "varchar(50)")
    @OneToMany
    private final List<MessageBody> messageBodies;

    public MessagePortion(int id, String subject, List<MessageBody> messageBodies) {
        this.id = id;
        this.subject = subject;
        this.messageBodies = messageBodies;
    }

    public MessagePortion(int id, String subject, String messageFrom, String sendingDate, List<MessageBody> messageBodies) {
        this.id = id;
        this.subject = subject;
        this.messageFrom = messageFrom;
        this.sendingDate = sendingDate;
        this.messageBodies = messageBodies;
    }

    public int getId() {
        return id;
    }
    public List<MessageBody> getMessageBodies() {
        return messageBodies;
    }
    public String getSubject() {
        return subject;
    }
    public String getMessageFrom() {
        return messageFrom;
    }
    public String getSendingDate() {
        return sendingDate;
    }
}
