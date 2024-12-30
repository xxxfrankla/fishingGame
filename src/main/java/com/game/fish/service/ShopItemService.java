package com.game.fish.service;

import com.game.fish.model.BoughtItem;
import com.game.fish.model.ShopItem;
import com.game.fish.model.User;
import com.game.fish.repository.BoughtItemRepository;
import com.game.fish.repository.ShopItemRepository;
import com.game.fish.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ShopItemService {

    @Autowired
    private ShopItemRepository shopItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoughtItemRepository boughtItemRepository;

    @Autowired
    private BoughtItemService boughtItemService;

    public ShopItem saveItem(ShopItem item){return shopItemRepository.save(item);}

    @Transactional
    public BoughtItem purchase(ShopItem item, int quantity, Long userId){
        Optional<User> user = userRepository.findByUserId(userId);
        Double userCoin = user.get().getCoins().doubleValue();
        if (quantity <= 0){
            throw new RuntimeException("Can not purchase 0 or less.");
        }
        Double price = item.getCoins() * quantity;
        if (userCoin < price){
            throw new RuntimeException("Not enough coins");
        }
        userRepository.subtractCoinsFromUser(BigDecimal.valueOf(price), userId);
        BoughtItem product = boughtItemService.purchaseItem(item, quantity, userId);
        return product;
    }

    public ShopItem findItemByCategoryAndName(String category, String itemName){
        return shopItemRepository.findItemByCategoryAndName(category, itemName);
    }
}