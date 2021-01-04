package com.rdesouter.dao.repository;


import com.rdesouter.model.SyncEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarEventRepo extends JpaRepository<SyncEvent, Integer> {

}
