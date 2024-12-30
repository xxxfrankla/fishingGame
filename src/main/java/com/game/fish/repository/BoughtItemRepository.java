package com.game.fish.repository;

import com.game.fish.model.BoughtItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoughtItemRepository extends JpaRepository<BoughtItem, Long> {
}
