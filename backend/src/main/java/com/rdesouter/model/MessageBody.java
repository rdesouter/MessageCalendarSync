package com.rdesouter.model;

import javax.persistence.*;
import java.util.List;

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
