package com.game.fish.repository;
import com.game.fish.model.FishCatch;
import com.game.fish.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.game.fish.model.Fish;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FishCatchRepository extends JpaRepository<FishCatch, Long>{
    boolean existsByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(fc.weight * fc.price), 0) FROM FishCatch fc WHERE fc.userId = :userId")
    BigDecimal calculateValue(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM FishCatch fc WHERE fc.userId = :userId")
    void deleteAllFish(Long userId);

    List<FishCatch> findAllByUserId(Long userId);
}