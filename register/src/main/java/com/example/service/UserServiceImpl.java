package com.example.service;

import com.example.dao.UserDao;
import com.example.model.User;
import com.example.validation.EmailExistsException;
import com.example.validation.UserExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void registerNewUserAccount(UserDto userDto) throws EmailExistsException, UserExistsException {

        if (userDao.findByName(userDto.getUsername()) != null) {
            throw new UserExistsException("username already exists: " + userDto.getUsername());
        }
        if (userDao.findUserByEmail(userDto.getEmail()) != null) {
            throw new EmailExistsException("email already exists: " + userDto.getEmail());
        }

        final User user = new User();
        user.setUsername(userDto.getUsername().trim());
        String hashed = passwordEncoder.encode(userDto.getPassword().trim());
        user.setPassword(hashed);
        user.setEnabled(true);
        user.setEmail(userDto.getEmail().trim());
        userDao.save(user);

        userDao.addAuthority(user.getUsername(), "ROLE_USER");
    }

    @Override
    public User findByName(String name) {
        return userDao.findByName(name);
    }

    @Override
    public void delete(String username) {
        userDao.delete(username);
    }
}
