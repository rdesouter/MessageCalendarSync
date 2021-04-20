package com.rdesouter.dao;

import com.rdesouter.dao.repository.MessageRepoCustom;
import com.rdesouter.model.SyncMessage;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
@Transactional
public class MessageDao implements MessageRepoCustom {

    @PersistenceContext
    EntityManager em;

    @Override
    public void findById(String id) {
        SyncMessage syncMessageList = em
                .createQuery("SELECT m FROM SyncMessage m WHERE m.id = :id", SyncMessage.class)
                .setParameter("id", id)
                .getSingleResult();

        System.out.println(syncMessageList.toString());
    }
}
