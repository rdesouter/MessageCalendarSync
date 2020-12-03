package com.rdesouter.security;

import com.rdesouter.dao.CandidateAuthenticatedUserDao;
import com.rdesouter.model.CandidateAuthenticatedUser;
import com.rdesouter.model.Role;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        CandidateAuthenticatedUser user = candidateAuthenticatedUserDao.findByName(username);
        for(Role role: user.roles){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.name));
        }

        return new User(user.name, user.password, grantedAuthorities);
    }
}
