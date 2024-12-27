package com.game.fish.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ChatGeneralService {

    String openaiApiKey = "";
    public String chatService(String quest){
        if (quest.isEmpty()){
            throw new IllegalArgumentException("Message is required");
        }

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openaiApiKey);
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("model", "gpt-3.5-turbo");
            requestData.put("messages", List.of(Map.of("role", "user", "content", quest)));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, headers);

            // 发送请求到 OpenAI API
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions",
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("choices")) {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                    if (!choices.isEmpty()) {
                        return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
                    }
                }
                return "No content";
            } else {
                throw new RuntimeException("Failed to get a response from OpenAI");
            }

        }catch(Exception E){
            throw new RuntimeException("Failed requesting for response");
        }
    }

//    @Value("${openai.api.key}") // 从配置文件加载 OpenAI API 密钥
//    private String openaiApiKey;

    public String matchKeywords(String message) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message is required");
        }

        try {
            // 关键字
            List<String> keyWords = List.of("fish", "play", "sell");
            String keyWordsStr = String.join(",", keyWords);

            // 生成 prompt
            String prompt = String.format(
                    "Given the keywords [%s], please match them to the message: \"%s\" and return the matched keywords. " +
                            "And return the closest matched keyword. Cannot return none. Just return the keyword without any description!",
                    keyWordsStr, message
            );

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openaiApiKey);

            // 设置请求体
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("model", "gpt-3.5-turbo");
            requestData.put("messages", List.of(
                    Map.of("role", "system", "content", "You are going to identify the keywords in a given sentence"),
                    Map.of("role", "user", "content", prompt)
            ));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, headers);

            // 发送请求到 OpenAI API
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions",
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("choices")) {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                    if (!choices.isEmpty()) {
                        return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
                    }
                }
                return "No content";
            } else {
                throw new RuntimeException("Failed to get a response from OpenAI");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error communicating with OpenAI: " + e.getMessage(), e);
        }
    }

    public String generateImage(String prompt) {
        if (prompt == null || prompt.isEmpty()) {
            throw new IllegalArgumentException("Prompt is required");
        }

        try {
            // 构建详细的 prompt
            String detailedPrompt = String.format(
                    "Given the keywords [%s], draw an image that relates to the keywords - requirements: Note: the image created should be more realistic, rather than in cartoon style.",
                    prompt
            );

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openaiApiKey);

            // 设置请求体
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("prompt", detailedPrompt);
            requestData.put("n", 1);
            requestData.put("size", "1024x1024");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, headers);

            // 发送请求到 OpenAI API
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/images/generations",
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("data")) {
                    List<Map<String, Object>> dataList = (List<Map<String, Object>>) responseBody.get("data");
                    if (!dataList.isEmpty()) {
                        return (String) dataList.get(0).get("url");
                    }
                }
                return "No content";
            } else {
                throw new RuntimeException("Failed to generate image");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error communicating with OpenAI: " + e.getMessage(), e);
        }
    }
}

