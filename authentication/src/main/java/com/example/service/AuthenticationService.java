package com.example.service;

import com.example.dao.UserDAO;
import com.example.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserInfo userInfo = userDAO.findByName(username);
        if (userInfo == null)
            throw new UsernameNotFoundException(username + " not exists!");
        User user = new User(userInfo.getUsername(), userInfo.getPassword(), userInfo.getAuthorities());
        return user;
    }
}
