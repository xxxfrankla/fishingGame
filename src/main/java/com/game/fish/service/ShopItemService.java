package com.game.fish.service;

import com.game.fish.model.BoughtItem;
import com.game.fish.model.PurchaseInfo;
import com.game.fish.model.ShopItem;
import com.game.fish.model.User;
import com.game.fish.repository.BoughtItemRepository;
import com.game.fish.repository.ShopItemRepository;
import com.game.fish.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ShopItemService {

    private static final String PURCHASE_QUEUE = "shop_purchase_queue";
    @Autowired
    private ShopItemRepository shopItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoughtItemRepository boughtItemRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BoughtItemService boughtItemService;

    public ShopItem saveItem(ShopItem item) {
        String itemCacheKey = "item_" + item.getCategory() + "_" + item.getName();

        // Check if the item is already cached
        ShopItem cachedItem = (ShopItem) redisTemplate.opsForValue().get(itemCacheKey);
        if (cachedItem != null) {
            return cachedItem;
        }

        // Save the item in the database
        ShopItem savedItem = shopItemRepository.save(item);

        // Cache the newly saved item
        redisTemplate.opsForValue().set(itemCacheKey, savedItem, 10, TimeUnit.MINUTES);

        return savedItem;
    }

    @Transactional
    public BoughtItem purchase(ShopItem item, int quantity, Long userId) {
        String lockKey = "shop_items_lock_" + userId + "_" + item.getName();
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        // Set lock with expiration of 10 seconds
        boolean lockAcquired = ops.setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
        if (!lockAcquired) {
            throw new RuntimeException("Too many requests! Try again later!");
        }

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

        BoughtItem i = null;
        if (!existingItems.isEmpty()) {
            // Aggregate quantity for the first found item
            boughtItemRepository.addQuantityToBoughtItem(quantity, item.getCategory(), item.getName(), userId);
            userRepository.subtractCoinsFromUser(BigDecimal.valueOf(price), userId);
             i = existingItems.get(0);
        } else {
            // Deduct coins from the user and create a new BoughtItem
            userRepository.subtractCoinsFromUser(BigDecimal.valueOf(price), userId);
            i = boughtItemService.purchaseItem(item, quantity, userId);
        }
        // Send purchase information to RabbitMQ queue
        PurchaseInfo purchaseInfo = new PurchaseInfo(userId, item.getName(), item.getCategory(), price);
        rabbitTemplate.convertAndSend(PURCHASE_QUEUE, purchaseInfo);

        return i;
    }


    public ShopItem findItemByCategoryAndName(String category, String itemName){
        return shopItemRepository.findItemByCategoryAndName(category, itemName);
    }
}
