package com.example.service;

import com.example.model.User;
import com.example.validation.EmailExistsException;
import com.example.validation.UserExistsException;

public interface UserService {
    void registerNewUserAccount(UserDto userDto) throws EmailExistsException, UserExistsException;

    User findByName(String name);

    void delete(String username);
}
