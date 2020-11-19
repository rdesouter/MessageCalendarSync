package com.rdesouter.controller;

import com.rdesouter.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

@Configuration
@EnableScheduling
@RequestMapping(value = "/message")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;
    /**
     *       ┌───────────── second (0-59)
     *       │ ┌───────────── minute (0 - 59)
     *       │ │ ┌───────────── hour (0 - 23)
     *       │ │ │ ┌───────────── day of the month (1 - 31)
     *       │ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)
     *       │ │ │ │ │ ┌───────────── day of the week (0 - 7)
     *       │ │ │ │ │ │          (0 or 7 is Sunday, or MON-SUN)
     *       │ │ │ │ │ │
     * cron= * * * * * *
     *
     * task will be executed each 30min between 9:00am-17:00pm
     * every day every month
     * bewteen monday to sunday
     *
     * */
//    @Scheduled(cron = "* 0/30 9-17 * * 0-7")
//    @Scheduled(cron = "0/10 0/1 9-17 * * 0-7")
    @GetMapping()
    public void getMessages() throws IOException, GeneralSecurityException {
        messageService.getMessages();
    }

    @GetMapping(value = "/logs")
    public void getLog() throws IOException, ParseException {
        messageService.lastMessageTimeStamp();
    }

    @PostMapping("/send-email")
    public void sendMail(@RequestBody String message) throws IOException, MessagingException {
        messageService.sendMail(message);
    }
}
