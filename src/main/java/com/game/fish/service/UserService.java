package com.game.fish.service;

import  com.game.fish.model.User;
import  com.game.fish.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

//implement method to be called by controller
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findUserById(String userId) {
        return userRepository.findByUserId(userId);
    }

    public boolean userExists(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
