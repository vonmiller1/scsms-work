package com.springboot.service;

import com.springboot.model.User;
import com.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User register(User user) {
        // Omitting password encryption for simplicity
        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user.get();
        }
        throw new RuntimeException("Invalid credentials");
    }
}
