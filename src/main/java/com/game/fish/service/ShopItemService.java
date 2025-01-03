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
import java.util.List;
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
    public BoughtItem purchase(ShopItem item, int quantity, Long userId) {
        // Validate user
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Double userCoin = user.get().getCoins().doubleValue();

        // Validate quantity
        if (quantity <= 0) {
            throw new RuntimeException("Cannot purchase 0 or less.");
        }

        // Calculate price and validate user's coins
        Double price = item.getCoins() * quantity;
        if (userCoin < price) {
            throw new RuntimeException("Not enough coins");
        }

        // Fetch existing items matching product type and name for the user
        List<BoughtItem> existingItems = boughtItemRepository.findByProductTypeAndProductName(item.getCategory(), item.getName());

        if (!existingItems.isEmpty()) {
            // Aggregate quantity for the first found item
            BoughtItem existingItem = existingItems.get(0); // Assume the first item is used for updating
            boughtItemRepository.addQuantityToBoughtItem(quantity, item.getCategory(), item.getName(), userId);
            return existingItem;
        } else {
            // Deduct coins from the user and create a new BoughtItem
            userRepository.subtractCoinsFromUser(BigDecimal.valueOf(price), userId);
            return boughtItemService.purchaseItem(item, quantity, userId);
        }
    }


    public ShopItem findItemByCategoryAndName(String category, String itemName){
        return shopItemRepository.findItemByCategoryAndName(category, itemName);
    }
}
