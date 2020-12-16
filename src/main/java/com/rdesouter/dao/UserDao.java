package com.rdesouter.dao;

import com.rdesouter.model.User;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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

    public List<User> findByLogin(String login){
        return em.createQuery("SELECT u FROM CandidateAuthenticatedUser u WHERE u.login = :login", User.class)
                .setParameter("login", login)
                .getResultList();
    }

    public void save(User user){
        em.persist(user);
    }
}
