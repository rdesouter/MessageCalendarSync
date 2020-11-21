package com.rdesouter.security;

import com.rdesouter.dao.UserDao;
import com.rdesouter.model.Role;
import com.rdesouter.model.UserWithRoles;
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

    private final UserDao userDao;

    public UserDetailServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        UserWithRoles user = userDao.findByName(username);
        for(Role role: user.roles){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.name));
        }

        return new User(user.name, user.password, grantedAuthorities);
    }
}
