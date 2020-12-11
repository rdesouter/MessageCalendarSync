package com.rdesouter.dao.repository;


import com.rdesouter.model.Message;

import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {


}
