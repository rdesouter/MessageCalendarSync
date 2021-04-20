package com.rdesouter.dao.repository;


import com.rdesouter.model.SyncMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends JpaRepository<SyncMessage, Integer> {

}
