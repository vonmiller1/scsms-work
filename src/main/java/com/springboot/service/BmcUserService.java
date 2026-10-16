package com.springboot.service;

import com.springboot.model.BmcUser;

public interface BmcUserService {
    BmcUser register(BmcUser user);
    BmcUser login(String email, String password);
}
