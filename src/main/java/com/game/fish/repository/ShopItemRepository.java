package com.game.fish.repository;

import com.game.fish.model.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
    boolean existsByName(String name);

    ShopItem findItemByCategoryAndName(String category, String itemName);
}
