package com.rdesouter.dao;

import com.rdesouter.model.CandidateAuthenticatedUser;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class CandidateAuthenticatedUserDao {

    private final HikariDataSource hikariDataSource;

    public CandidateAuthenticatedUserDao(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    @PersistenceContext
    EntityManager em;

    public List<CandidateAuthenticatedUser> findByLogin(String login){
        return em.createQuery("SELECT u FROM CandidateAuthenticatedUser u WHERE u.login = :login", CandidateAuthenticatedUser.class)
                .setParameter("login", login)
                .getResultList();
    }
}
