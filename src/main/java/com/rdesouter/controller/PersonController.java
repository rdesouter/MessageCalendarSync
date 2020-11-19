package com.rdesouter.controller;

import com.rdesouter.dao.PersonDao;
import com.rdesouter.model.Person;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    private final PersonDao personDao;

    public PersonController(PersonDao personDao) {
        this.personDao = personDao;
    }

     @PostMapping("/person")
    public void addPerson(@RequestBody Person person){
        personDao.insert(person);
     }
}