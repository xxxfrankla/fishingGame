package com.game.fish.service;

import com.game.fish.model.FishCatch;
import com.game.fish.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.game.fish.model.Fish;
import com.game.fish.repository.FishRepository;
import com.game.fish.repository.FishCatchRepository;
import com.game.fish.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
//鱼的模板
@Service
public class FishService {

    @Autowired
    private FishRepository fishRepository;

    @Autowired
    private FishCatchRepository fishCatchRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Fish> getAllActiveFishes() {
        return fishRepository.findByStatus(true); //status: whether the fish should be active for
        // specific events
    }

    public Fish saveFish(Fish fish) {
        return fishRepository.save(fish);
    }

    //pick a fish from pool

    @Transactional
    public BigDecimal sellFish(Long userId){
        if(!fishCatchRepository.existsByUserId(userId)){
            return BigDecimal.ZERO;
        }
        //总价值，卖掉，加钱
        BigDecimal coin = fishCatchRepository.calculateValue(userId);
        if (coin == null){
            coin = BigDecimal.ZERO;
        }
        fishCatchRepository.deleteAllFish(userId);
        userRepository.addCoinToUser(coin, userId);
        return coin;
    }
}