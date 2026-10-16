package com.springboot.service;

import com.springboot.model.BmcUser;
import com.springboot.repository.BmcUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BmcUserServiceImpl implements BmcUserService {

    @Autowired
    private BmcUserRepository bmcUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public BmcUser register(BmcUser user) {
        if (bmcUserRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        if (bmcUserRepository.existsByBmcId(user.getBmcId())) {
            throw new RuntimeException("BMC ID already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return bmcUserRepository.save(user);
    }

    @Override
    public BmcUser login(String email, String password) {
        Optional<BmcUser> user = bmcUserRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user.get();
        }
        throw new RuntimeException("Invalid credentials");
    }
}
