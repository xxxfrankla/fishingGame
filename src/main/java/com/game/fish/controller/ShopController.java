package com.game.fish.controller;

import com.game.fish.model.BoughtItem;
import com.game.fish.model.ShopItem;
import com.game.fish.repository.ShopItemRepository;
import com.game.fish.service.ShopItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//使用coin购买商品
//
@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ShopItemRepository shopItemRepository;

    @Autowired
    private ShopItemService shopItemService;

    @PostMapping("/create")
    public ResponseEntity<ShopItem> createShopItem(@RequestBody ShopItem shopItem){
        ShopItem product = shopItemService.saveItem(shopItem);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/purchase")
    public ResponseEntity<BoughtItem> purchase(@RequestParam String category,String productName,
                                             int amount,
                                           Long id, String userEmail, String username){
        ShopItem item = shopItemService.findItemByCategoryAndName(category, productName);
        BoughtItem product = shopItemService.purchase(item, amount, id, userEmail, username);
        return ResponseEntity.ok(product);
    }
}
