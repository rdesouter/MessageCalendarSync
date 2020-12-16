package com.rdesouter.controller;

import com.rdesouter.config.AppConfiguration;

import com.rdesouter.model.AuthRequest;
import com.rdesouter.model.AuthResponse;
import com.rdesouter.model.User;
import com.rdesouter.security.SecurityConfigurer;
import com.rdesouter.service.UserService;
import com.rdesouter.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;
    private final AppConfiguration appConfiguration;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private SecurityConfigurer securityConfigurer;

    public UserController(UserService userService, AppConfiguration appConfiguration) {
        this.userService = userService;
        this.appConfiguration = appConfiguration;
    }

    @GetMapping(value = "/sign-in")
    public String login(){
        return "You've successful sign in to application";
    }

    @PostMapping("/create")
    public void addPerson(@RequestBody User user) {
        User emptyUser = new User((short) 1, "toto", "password", "");
        this.userService.create(emptyUser);
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Incorrect username or password caused by " + e);
//            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @GetMapping("/testInsert")
    public void testTable() {
        this.userService.testInsertTable();
    }
}
