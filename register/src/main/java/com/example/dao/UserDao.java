package com.example.dao;

import com.example.model.User;

public interface UserDao {
    User findUserByEmail(String email);

    User findByName(String name);

    void addAuthority(String username, String role);

    void save(User user);

    void delete(String username);
}
