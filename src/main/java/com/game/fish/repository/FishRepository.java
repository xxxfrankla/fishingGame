package com.game.fish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.game.fish.model.Fish;

import java.util.List;
//所有的鱼
@Repository
public interface FishRepository extends JpaRepository<Fish, Long> {
    List<Fish> findByStatus(boolean status); //看这个鱼是否生效
}