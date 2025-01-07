package com.game.fish.service;

import  com.game.fish.model.User;
import  com.game.fish.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

//implement method to be called by controller
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findUserById(Long userId) {
        return userRepository.findByUserId(userId);
    }

    public boolean userExists(Long userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

    public User createUser(User user) {
        user.setCoins(BigDecimal.ZERO);
        user.setDiamonds(0);
        user.setLevel(1);
        user.setCurrentExperience(0);
        user.setExperienceForNextLevel(100);
        user.setRodType("basic");
        user.setFishInventory(List.of());
        return userRepository.save(user);
    }
}
