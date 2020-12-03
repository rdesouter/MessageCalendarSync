package com.rdesouter.controller;

import com.rdesouter.config.AppConfiguration;
import com.rdesouter.dao.AuthenticatedUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final AuthenticatedUserDao authenticatedUserDao;
    private final AppConfiguration appConfiguration;

    public UserController(AuthenticatedUserDao authenticatedUserDao, AppConfiguration appConfiguration) {
        this.authenticatedUserDao = authenticatedUserDao;
        this.appConfiguration = appConfiguration;
    }



     @PostMapping("/candidateUser")
    public void addPerson(@RequestBody com.rdesouter.model.AuthenticatedUser authenticatedUser){
        this.authenticatedUserDao.insert(authenticatedUser);
     }
}
