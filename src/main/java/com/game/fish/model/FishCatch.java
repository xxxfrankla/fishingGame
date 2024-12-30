package com.game.fish.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class FishCatch {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Auto-generate unique IDs
    private Long id; // Unique primary key for each fish catch

    @Column(nullable = false)
    private Long userId; // Associate fish with a specific user

    @Column(name = "fish_type_id", nullable = false)
    private String fishType;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private double price;

    @Column(name = "rarity_level", length = 50, nullable = false)
    private String rarityLevel;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "caught_at", nullable = false, updatable = false)
    private LocalDateTime caughtAt;

    public Long getUser() {
        return userId;
    }

    public void setUser(Long userId) {
        this.userId = userId;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFishType() {
        return fishType;
    }

    public void setFishType(String fishType) {
        this.fishType = fishType;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getRarityLevel() {
        return rarityLevel;
    }

    public void setRarityLevel(String rarityLevel) {
        this.rarityLevel = rarityLevel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCaughtAt() {
        return caughtAt;
    }

    public void setCaughtAt(LocalDateTime caughtAt) {
        this.caughtAt = caughtAt;
    }
}
