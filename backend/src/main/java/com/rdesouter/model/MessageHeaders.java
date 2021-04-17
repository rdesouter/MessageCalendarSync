package com.rdesouter.model;

public class MessageHeaders {

    private String subject;
    private String sendingDate;
    private String from;
    private String to;


    public MessageHeaders(String subject, String sendingDate, String from, String to) {
        this.subject = subject;
        this.sendingDate = sendingDate;
        this.from = from;
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }
    public String getSendingDate() {
        return sendingDate;
    }
    public String getFrom() {
        return from;
    }
    public String getTo() {
        return to;
    }
}
