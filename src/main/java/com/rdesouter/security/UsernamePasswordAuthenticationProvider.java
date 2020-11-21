package com.rdesouter.security;

import com.rdesouter.dao.UserDao;
import com.rdesouter.model.Role;
import com.rdesouter.model.UserWithRoles;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDao userDao;

    public UsernamePasswordAuthenticationProvider(BCryptPasswordEncoder bCryptPasswordEncoder, UserDao userDao) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDao = userDao;
    }

    @Override
    public Authentication authenticate(Authentication authentication){
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String password = (String) token.getCredentials();
        String username = (String) token.getPrincipal();
        String hashedPassword = bCryptPasswordEncoder.encode(password);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        UserWithRoles user = userDao.findByName(username);
        if(user == null || !hashedPassword.equals(user.password)){
            throw new BadCredentialsException("User name or password is not valid.");
        }

        for(Role role: user.roles){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.name));
        }

        return new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication){
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
