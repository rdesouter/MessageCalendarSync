package com.rdesouter.service;

import com.rdesouter.dao.repository.AuthenticadUserRepository;
import com.rdesouter.model.AuthenticatedUser;
import com.rdesouter.model.CalendarEvent;
import com.rdesouter.model.Message;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Transactional
public class AuthenticatedUserService {

    @Autowired
    private AuthenticadUserRepository authUserRepo;


    public void testInsertTable() {

        LocalDateTime begin = LocalDateTime.of(2020,7,1,7,0,0);
        LocalDateTime finish = LocalDateTime.of(2020,7,1,9,30,0);

        CalendarEvent calendar1 = new CalendarEvent(
                UUID.randomUUID().toString(),
                "premier RDV",
                "description pour le rendez-vous",
                "rue de l'imaginaire 6,9000 La Lune",
                begin,
                finish,
                Arrays.asList(new String[]{"ron.desouter@gmail.com","j-r@gmail.com", "rick@morty.com"}));
        CalendarEvent calendar2 = new CalendarEvent(UUID.randomUUID().toString(), "anniversaire");
        CalendarEvent calendar3 = new CalendarEvent(UUID.randomUUID().toString(), "nouvel an");

        List<Message> messages = new ArrayList<>();
        Message message1 = new Message(
                "blabla premier message",
                calendar1);
        Message message2 = new Message(
                "U2FsdXRhdGlvbnMgZGlzdGluZ3XDqWVzDQoNClLDqXN1bcOpIDogcmVuZGV6LXZvdXMgcHJpcyB2aWEgbCdBUEkNCkFkcmVzc2UgOiBydWUgZHUgdG91Y2FuLCAxMzUwIE5vZHV3ZXosIEJlbGdpcXVlDQoNClTDqWzDqXBob25lIDogMDQ4NC43MjYuNDc5DQpEYXRlIGRlIGTDqWJ1dCA6IDIwLjExLjIwMjANCkRhdGUgZGUgZmluIDogMjEuMTEuMjAyMA0KDQpGaW4gZHUgbWVzc2FnZSBxdWkgbidlc3QgcGFzIHByaXMgZW4gY29tcHRlDQoNCkNkbHQNClJvbmFsZCBEZSBTb3V0ZXI",
                calendar2);
        messages.add(message1);
        messages.add(message2);

        List<Message> messages1 = new ArrayList<>();
        Message message3 = new Message(
                "TG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2NpbmcgZWxpdC4gDQpOdW5jIGVsZW1lbnR1bSBtYWduYSBlZ2V0IGVuaW0gb3JuYXJlLCBlZ2V0IHRpbmNpZHVudCBuaXNsIHByZXRpdW0uIFN1c3BlbmRpc3NlIGlkIGF1Y3RvciBtZXR1cy4gDQpTZWQgcXVpcyBydXRydW0gdXJuYS4",
                calendar3);
        messages1.add(message3);

        List<CalendarEvent> calendars = new ArrayList<>();
        calendars.add(calendar1);
        calendars.add(calendar2);

        List<CalendarEvent> calendars1 = new ArrayList<>();
        calendars1.add(calendar3);

        authUserRepo.save(new AuthenticatedUser("ronald", "test123", "admin", messages, calendars));
        authUserRepo.save(new AuthenticatedUser("jean-robert", "jr-123", "guest",  messages1, calendars1));
    }
}
