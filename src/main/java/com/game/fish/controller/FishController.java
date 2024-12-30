package com.game.fish.controller;

import com.game.fish.model.FishCatch;
import com.game.fish.repository.FishCatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.game.fish.model.Fish;
import com.game.fish.service.FishService;
import com.game.fish.service.FishCatchService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/fish")
public class FishController {

    @Autowired
    private FishService fishService;

    @Autowired
    private FishCatchService fishCatchService;

    @Autowired
    private FishCatchRepository fishCatchRepository;

    @GetMapping
    public ResponseEntity<List<Fish>> getAllActiveFishes() {
        List<Fish> fishes = fishService.getAllActiveFishes();
        return ResponseEntity.ok(fishes);
    }

    @PostMapping("/create")
    public ResponseEntity<Fish> createFish(@RequestBody Fish fish) {
        Fish savedFish = fishService.saveFish(fish);
        return ResponseEntity.ok(savedFish);
    }

    @PostMapping("/catch-fish")
    public ResponseEntity<FishCatch> catchFish(@RequestParam Long userId){
        FishCatch caughtFish = fishCatchService.catchFish(userId);
        return ResponseEntity.ok(caughtFish);
    }


    @PostMapping("/sell-fish")
    public ResponseEntity<BigDecimal> sellFish(@RequestParam Long userId) {
        BigDecimal total = fishService.sellFish(userId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/fish-catches")
    public ResponseEntity<List<FishCatch>> getFishCatches(@RequestParam Long userId) {
        List<FishCatch> fishCatches = fishCatchRepository.findAllByUserId(userId);
        return ResponseEntity.ok(fishCatches);
    }
}