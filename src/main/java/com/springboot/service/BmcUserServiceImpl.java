package com.springboot.service;

import com.springboot.model.BmcUser;
import com.springboot.repository.BmcUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BmcUserServiceImpl implements BmcUserService {

    @Autowired
    private BmcUserRepository bmcUserRepository;

    @Override
    public BmcUser register(BmcUser user) {
        return bmcUserRepository.save(user);
    }

    @Override
    public BmcUser login(String email, String password) {
        Optional<BmcUser> user = bmcUserRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user.get();
        }
        throw new RuntimeException("Invalid credentials");
    }
}
