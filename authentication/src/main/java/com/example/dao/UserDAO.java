package com.example.dao;

import com.example.model.UserInfo;

public interface UserDAO {
    UserInfo findByName(String username);
}
