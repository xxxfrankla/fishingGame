package com.game.fish.repository;

import com.game.fish.model.BoughtItem;
import com.game.fish.model.ShopItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoughtItemRepository extends JpaRepository<BoughtItem, Long> {
    List<BoughtItem> findByProductTypeAndProductName(String productType, String productName);

    @Transactional
    @Modifying
    @Query("UPDATE BoughtItem i SET i.quantity = i.quantity + :quantity WHERE i.userId = :userId AND i.productType = :productType AND i.productName = :productName")
    void addQuantityToBoughtItem(@Param("quantity") int quantity,
                                 @Param("productType") String productType,
                                 @Param("productName") String productName,
                                 @Param("userId") Long userId);

}
