package com.rdesouter.message.service;

import java.time.LocalDateTime;

public interface MessageConstant {

    String SENDER = "ron.desouter@gmail.com";
    String RECEIVER = "dev@papymousse.be";
    String SUBJECT = "Message sent with gmail API";
    String BODY_TITLE = "Congratutaltions";
    String BODY_TEXT = "this is a test message sending with Gmail API sending at: " + LocalDateTime.now();
}
