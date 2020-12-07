package com.rdesouter.security;

import com.rdesouter.dao.CandidateAuthenticatedUserDao;
import com.rdesouter.model.CandidateAuthenticatedUser;
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
    private final CandidateAuthenticatedUserDao candidateAuthenticatedUserDao;

    public UsernamePasswordAuthenticationProvider(BCryptPasswordEncoder bCryptPasswordEncoder, CandidateAuthenticatedUserDao candidateAuthenticatedUserDao) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.candidateAuthenticatedUserDao = candidateAuthenticatedUserDao;
    }

    @Override
    public Authentication authenticate(Authentication authentication){
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String password = (String) token.getCredentials();
        String username = (String) token.getPrincipal();
        String hashedPassword = bCryptPasswordEncoder.encode(password);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        CandidateAuthenticatedUser user = candidateAuthenticatedUserDao.findByLogin(username).get(0);

        if(user == null || !hashedPassword.equals(user.getPassword())){
            throw new BadCredentialsException("User name or password is not valid.");
        }

        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication){
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
