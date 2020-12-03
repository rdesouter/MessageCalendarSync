package com.rdesouter.service;

import com.rdesouter.dao.repository.CalendarEventRepository;
import com.rdesouter.model.CalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CalendarService {

    @Autowired
    private CalendarEventRepository calendarEventRepository;


    public List<CalendarEvent> listAll(){
        return calendarEventRepository.findAll();
    }
}
