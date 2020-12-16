package com.rdesouter.service;

import com.rdesouter.dao.repository.UserRepo;
import com.rdesouter.model.User;
import com.rdesouter.model.CalendarEvent;
import com.rdesouter.model.Message;
import com.rdesouter.security.SecurityConfigurer;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Transactional
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SecurityConfigurer securityConfigurer;
    @Autowired
    private HikariDataSource hikariDataSource;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        String hashPwd = securityConfigurer.passwordEncoder().encode("ronald");
        //TODO find by username with dao
        return new org.springframework.security.core.userdetails.User("ronald", hashPwd, new ArrayList<>());
    }

    public void create(User user){
        userRepo.save(user);
    }

    public void insert(User user){
        try(
                Connection connection = hikariDataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("" +
                        "INSERT INTO public.person (name, email, refresh_token) VALUES(?,?,?)")
        ){
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getToken());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

//    Test purpose
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

        userRepo.save(new User(
                "toto@gmail.com",
                "test123",
                "admin",
                "",
                messages,
                calendars)
        );
        userRepo.save(new User(
                "jr@google.com",
                "jr-123",
                "guest",
                "",
                messages1,
                calendars1)
        );
    }


}
