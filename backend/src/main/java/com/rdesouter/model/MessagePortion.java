package com.rdesouter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "message_portion", schema = "public")
public class MessagePortion {
//    @JsonIgnore
    @Id
    @GeneratedValue
    private int id;
    @Column(columnDefinition="varchar(255)")
    private String subject;
    @Column(columnDefinition="varchar(255)")
    private String messageFrom;
    @Column(columnDefinition="varchar(50)")
    private String sendingDate;
    @OneToMany(cascade = CascadeType.ALL)
    private List<MessageBody> messageBodies;

    public MessagePortion() {
    }

    public MessagePortion(int id, String subject, List<MessageBody> messageBodies) {
        this.id = id;
        this.subject = subject;
        this.messageBodies = messageBodies;
    }

    public MessagePortion(String subject, String messageFrom, String sendingDate, List<MessageBody> messageBodies) {
        this.subject = subject;
        this.messageFrom = messageFrom;
        this.sendingDate = sendingDate;
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
