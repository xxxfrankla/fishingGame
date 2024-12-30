package com.game.fish.model;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class User {

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getCoins() {
        return coins;
    }

    public void setCoins(BigDecimal coins) {
        this.coins = coins;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurrentExperience() {
        return currentExperience;
    }

    public void setCurrentExperience(int currentExperience) {
        this.currentExperience = currentExperience;
    }

    public int getExperienceForNextLevel() {
        return experienceForNextLevel;
    }

    public void setExperienceForNextLevel(int experienceForNextLevel) {
        this.experienceForNextLevel = experienceForNextLevel;
    }

    public String getRodType() {
        return rodType;
    }

    public void setRodType(String rodType) {
        this.rodType = rodType;
    }

    public List<String> getFishInventory() {
        return fishInventory;
    }

    public void setFishInventory(List<String> fishInventory) {
        this.fishInventory = fishInventory;
    }

    @Id
    @Column(unique = true, nullable = false)
    private Long userId; // 用户ID

    private String userName; // 用户名
    private BigDecimal coins = BigDecimal.ZERO; // 用户金币数量
    private int diamonds; // 用户钻石数量
    private int level; // 用户等级
    private int currentExperience; // 当前经验值
    private int experienceForNextLevel; // 升级所需经验值
    private String rodType; // 钓鱼竿类型

    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> fishInventory; // 鱼类库存
}
