package com.game.fish.service;

import com.game.fish.model.Fish;
import com.game.fish.model.FishCatch;
import com.game.fish.repository.FishCatchRepository;
import com.game.fish.repository.FishRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;


@Service
public class FishCatchService {
    @Autowired
    private FishRepository fishRepository;

    @Autowired
    private FishCatchRepository fishCatchRepository;
    //catch fish
    @Transactional
    public FishCatch catchFish(Long userId){
        List<Fish> fishList = fishRepository.findByStatus(true);
        if (fishList.isEmpty()) {
            throw new IllegalStateException("No active fishes available to catch.");
        }
        //1. probability helper, 帮助选出应该抓到的鱼
        Fish caughtFish = probHelper(fishList);
        double weight = weightGenerator(caughtFish);
        String img = imageHelper();
        FishCatch fishCatch = new FishCatch();
        fishCatch.setUser(userId);
        fishCatch.setFishType(caughtFish.getType());
        fishCatch.setImageUrl(img);
        fishCatch.setWeight(weight);
        LocalDateTime now = LocalDateTime.now();
        fishCatch.setCaughtAt(now);
        String rarityLevel = evaluate(weight, caughtFish);
        fishCatch.setRarityLevel(rarityLevel);
        double price = calculatePrice(rarityLevel, weight);
        fishCatch.setPrice(price);
        return fishCatchRepository.save(fishCatch);
    }

    public double calculatePrice(String rarityLevel, double weight){
        double basePrice;

        switch (rarityLevel) {
            case "SS":
                basePrice = 10.0; // High base price for rare fish
                break;
            case "S":
                basePrice = 8.0;
                break;
            case "A":
                basePrice = 5.0;
                break;
            case "B":
                basePrice = 3.0;
                break;
            case "C":
                basePrice = 1.0;
                break;
            default:
                throw new IllegalArgumentException("Unknown rarity level: " + rarityLevel);
        }

        // Calculate total price based on weight
        return basePrice * weight;
    }
    public String evaluate(double weight, Fish fish) {
        double sWeight = fish.getsWeight() != null ? fish.getsWeight() : 20.0;
        double aWeight = fish.getaWeight() != null ? fish.getaWeight() : 19.0;
        double bWeight = fish.getbWeight() != null ? fish.getbWeight() : 18.0;
        double cWeight = fish.getcWeight() != null ? fish.getcWeight() : 17.0;

        if (weight > sWeight) {
            return "SS";
        } else if (weight > aWeight) {
            return "S";
        } else if (weight > bWeight) {
            return "A";
        } else if (weight > cWeight) {
            return "B";
        } else {
            return "C";
        }
    }

    private Fish probHelper(List<Fish> fishList){
        double totalProbability = fishList.stream().mapToDouble(Fish::getProbability).sum();
        double randomProbability = Math.random() * totalProbability;
        double curProb = 0.0;

        for(Fish fish : fishList){
            curProb += fish.getProbability();
            if (curProb >= randomProbability){
                return fish;
            }
        }
        throw new IllegalStateException("No fish could be selected. Check probabilities.");
    }

    private double weightGenerator(Fish fish) {
        double mean = fish.getMean() != null ? fish.getMean() : 1.75;
        double stdDev = fish.getStandardDeviation() != null ? fish.getStandardDeviation() : 0.625;

        Random random = new Random();
        double u1 = random.nextDouble();
        double u2 = random.nextDouble();
        double randStdNormal = Math.sqrt(-2.0 * Math.log(u1)) * Math.sin(2.0 * Math.PI * u2);

        return mean + stdDev * randStdNormal;
    }

    private String imageHelper(){
        return "0";
    }
}
