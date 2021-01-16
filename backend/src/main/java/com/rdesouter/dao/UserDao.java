package com.rdesouter.dao;

import com.rdesouter.dao.repository.UserRepo;
import com.rdesouter.model.User;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.util.List;

@Component
@Transactional
public class UserDao {

    private final HikariDataSource hikariDataSource;

    public UserDao(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    @PersistenceContext
    EntityManager em;

    public User findByLogin(String login){
        List<User> user = em.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                      .setParameter("login", login)
                      .getResultList();
        return user.get(0);

    }
}
