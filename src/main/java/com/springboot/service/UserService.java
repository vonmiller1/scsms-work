package com.springboot.service;

import com.springboot.model.User;

public interface UserService {
    User register(User user);
    User login(String username, String password);
}
