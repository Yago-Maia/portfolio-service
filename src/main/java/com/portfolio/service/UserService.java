package com.portfolio.service;

import com.portfolio.security.UserSS;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserSS loadUserByUsernameAndRole(String username, String role, Long id) {
        return UserSS.builder()
                .username(username)
                .password("")
                .roles(role)
                .idUser(id)
                .build();
    }

}
