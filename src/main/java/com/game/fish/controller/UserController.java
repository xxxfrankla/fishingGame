package com.game.fish.controller;

import com.game.fish.model.User;
import com.game.fish.service.UserService;
import com.game.fish.service.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FishService fishService;


    @GetMapping
    public Map<String, Object> defaultMapping() {
        return Map.of(
                "message", "Welcome to the User API. Available endpoints: /is-exist, /create, /generate-token, /basic, etc."
        );
    }

    // 用户存在检查
    @GetMapping("/is-exist")
    public Map<String, Object> checkUserExist(@RequestParam Long userId) {
        boolean exists = userService.userExists(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", exists ? 200 : 404);
        response.put("msg", exists ? "User exists" : "User not found");
        response.put("data", Map.of("exist", exists ? 1 : 0));
        return response;
    }

    @PostMapping("/create")
    public Map<String, Object> createUser(@RequestBody User user) {
        if (user.getUserId() == null || user.getUserName() == null) {
            return Map.of("code", 400, "msg", "Parameter error");
        }

        if (userService.userExists(user.getUserId())) {
            return Map.of("code", 409, "msg", "Already exist");
        }

        user.setCoins(BigDecimal.ZERO);
        user.setDiamonds(0);
        user.setLevel(1);
        user.setCurrentExperience(0);
        user.setExperienceForNextLevel(100);
        user.setRodType("basic");
        user.setFishInventory(List.of());

        userService.createUser(user);
        return Map.of("code", 201, "msg", "Create Successful");

    }

    private static final String API_PASSWORD = "default_password";
    @GetMapping("/generate-token")
    public Map<String, String> generateToken(@RequestBody User user){
        long timestamp = System.currentTimeMillis() / 1000;
        String token = generateToken(API_PASSWORD, String.valueOf(timestamp));
        return Map.of("token", token, "time", String.valueOf(timestamp));
    }

    private String generateToken(String password, String timestamp) {
        try {
            String data = password + timestamp + password;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    @GetMapping("/basic")
    public Map<String, Object> getUserBasic(@RequestParam Long userId) {
        Optional<User> user = userService.findUserById(userId);
        if (user.isEmpty()) {
            return Map.of("code", 404, "msg", "User not found");
        }
        return Map.of(
                "code", 200,
                "msg", "Success",
                "data", Map.of(
                        "userName", user.get().getUserName(),
                        "rodType", user.get().getRodType()
                )
        );
    }

    @GetMapping("/finance")
    public Map<String, Object> getUserFinance(@RequestParam Long userId) {
        //coins, diamonds
        Optional<User> user = userService.findUserById(userId);
        if (user.isEmpty()) {
            return Map.of("code", 404, "msg", "User not found");
        }
        return Map.of(
                "code", 200,
                "msg", "Success",
                "data", Map.of(
                        "coins", user.get().getCoins(),
                        "diamonds", user.get().getDiamonds()
                )
        );
    }

    @GetMapping("/level")
    public Map<String, Object> getUserLevel(@RequestParam Long userId) {
        //level, current exp, exp to next level
        Optional<User> user = userService.findUserById(userId);
        if (user.isEmpty()) {
            return Map.of("code", 404, "msg", "User not found");
        }
        return Map.of(
                "code", 200,
                "msg", "Success",
                "data", Map.of(
                        "level", user.get().getLevel(),
                        "currentExperience", user.get().getCurrentExperience(),
                        "experienceForNextLevel", user.get().getExperienceForNextLevel()
                )
        );
    }

    @GetMapping("/inventory")
    public Map<String, Object> getUserInventory(@RequestParam Long userId) {
        //fish inventory
        Optional<User> user = userService.findUserById(userId);
        if (user.isEmpty()) {
            return Map.of("code", 404, "msg", "User not found");
        }
        return Map.of(
                "code", 200,
                "msg", "Success",
                "data", Map.of(
                        "fishInventory", user.get().getFishInventory()
                )
        );
    }



}
