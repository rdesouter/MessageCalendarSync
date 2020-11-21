package com.rdesouter.controller;

import com.rdesouter.config.AppConfiguration;
import com.rdesouter.dao.PersonDao;
import com.rdesouter.model.Person;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    private final PersonDao personDao;
    private final AppConfiguration appConfiguration;

    public PersonController(PersonDao personDao, AppConfiguration appConfiguration) {
        this.personDao = personDao;
        this.appConfiguration = appConfiguration;
        System.out.println(new Long(appConfiguration.getTokenExpirationTime()));
    }

     @PostMapping("/person")
    public void addPerson(@RequestBody Person person){
        personDao.insert(person);
     }
}
