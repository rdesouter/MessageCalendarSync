package com.rdesouter.controller;

import com.rdesouter.dao.UserDao;
import com.rdesouter.model.SyncMessage;
import com.rdesouter.model.User;
import com.rdesouter.service.SyncMessageService;
import com.rdesouter.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.List;

@EnableScheduling
@CrossOrigin("http://localhost:4200")
@RequestMapping(value = "/message")
@RestController
public class SyncMessageController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SyncMessageService syncMessageService;
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
    public List<SyncMessage> getMessages(HttpServletRequest request) throws IOException {

        final String authHeader = request.getHeader("Authorization");

        User connected = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractUserName(jwt);
            connected = userDao.findByLogin(username);
        }
        return syncMessageService.getMessages(connected);
    }

    @GetMapping(value = "/logs")
    public void getLog() throws IOException, ParseException {
        syncMessageService.lastMessageTimeStamp();
    }

    @PostMapping("/send-email")
    public void sendMail(@RequestBody String message) throws IOException, MessagingException {
        syncMessageService.sendMail(message);
    }
}
