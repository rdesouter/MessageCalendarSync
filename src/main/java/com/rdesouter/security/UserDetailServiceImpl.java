package com.rdesouter.security;

import com.rdesouter.dao.CandidateAuthenticatedUserDao;
import com.rdesouter.model.CandidateAuthenticatedUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    private final CandidateAuthenticatedUserDao candidateAuthenticatedUserDao;

    public UserDetailServiceImpl(CandidateAuthenticatedUserDao candidateAuthenticatedUserDao) {
        this.candidateAuthenticatedUserDao = candidateAuthenticatedUserDao;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {

        List<CandidateAuthenticatedUser> users = candidateAuthenticatedUserDao.findByLogin(username);
        if(users.size() == 0){
            throw new UsernameNotFoundException("user not found by login string");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(users.get(0).getRole()));

        return new User(users.get(0).getLogin(), users.get(0).getPassword(), grantedAuthorities);
    }
}
