package com.game.fish.repository;

import  com.game.fish.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String userId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.coins = u.coins + :coin WHERE u.userId = :userId")
    void addCoinToUser(@Param("coin") BigDecimal coin, @Param("userId") Long userId);
}

