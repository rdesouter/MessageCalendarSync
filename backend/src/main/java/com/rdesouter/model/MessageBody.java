package com.rdesouter.model;

import javax.persistence.*;

@Entity
@Table(name = "bodies", schema = "public")
public class MessageBody {
    @Id
    @GeneratedValue
    private int id;
    @Column(columnDefinition="TEXT")
    private String payload;
    @Column(columnDefinition = "varchar(20)")
    private String mimeType;


    public MessageBody() {
    }

    public MessageBody(String payload, String mimeType) {
        this.payload = payload;
        this.mimeType = mimeType;
    }

    public MessageBody(int id, String payload, String mimeType) {
        this.id = id;
        this.payload = payload;
        this.mimeType = mimeType;
    }

    public int getId() {
        return id;
    }
    public String getPayload() {
        return payload;
    }
    public String getMimeType() {
        return mimeType;
    }
}
