package com.game.fish.service;

import com.game.fish.model.BoughtItem;
import com.game.fish.model.ShopItem;
import com.game.fish.repository.BoughtItemRepository;
import com.game.fish.repository.ShopItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class BoughtItemService {
    @Autowired
    private ShopItemRepository shopItemRepository;

    @Autowired
    private BoughtItemRepository boughtItemRepository;

    @Transactional
    public BoughtItem purchaseItem(ShopItem item, int quantity, Long userId){
        BoughtItem product = new BoughtItem();
        product.setUserId(userId);
        product.setProductName(item.getName());
        product.setProductType(item.getCategory());
        product.setQuantity(quantity);
        LocalDateTime now = LocalDateTime.now();
        product.setPurchaseDate(now);
        return boughtItemRepository.save(product);
    }
}
