package com.rdesouter.controller;

import com.rdesouter.service.MessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send-email")
    public void sendMail(@RequestBody String message) throws IOException, MessagingException {
        messageService.sendMail(message);
    }
}
