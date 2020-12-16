package com.rdesouter.controller;

import com.rdesouter.config.AppConfiguration;
import com.rdesouter.dao.AuthenticatedUserDao;
import com.rdesouter.service.AuthenticatedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class CandidateAuthenticadUserController {

    private final AuthenticatedUserDao authenticatedUserDao;
    private final AppConfiguration appConfiguration;

    @Autowired
    private AuthenticatedUserService authUserService;

    public CandidateAuthenticadUserController(AuthenticatedUserDao authenticatedUserDao, AppConfiguration appConfiguration) {
        this.authenticatedUserDao = authenticatedUserDao;
        this.appConfiguration = appConfiguration;
    }

    @PostMapping("/candidateUser")
    public void addPerson(@RequestBody com.rdesouter.model.AuthenticatedUser authenticatedUser){
        this.authenticatedUserDao.insert(authenticatedUser);
     }

    @GetMapping("/testInsert")
    public void testTable(){
        this.authUserService.testInsertTable();
    }
}
