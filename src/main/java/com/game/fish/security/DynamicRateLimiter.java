package com.game.fish.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DynamicRateLimiter implements Filter {

    private final Map<String, Long> userRequestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> userLastRequestTime = new ConcurrentHashMap<>();
    private static final long RATE_LIMIT_WINDOW_SECONDS = 1; // 1 second
    private static final long MAX_REQUESTS_PER_WINDOW = 5;

    private boolean shouldApplyRateLimiting(String path) {
        // Add paths that should be excluded from rate limiting
        List<String> excludedPaths = List.of("/user/create", "/user/generate-token");

        return !excludedPaths.contains(path);
    }

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (!shouldApplyRateLimiting(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        String userId = httpRequest.getParameter("userId"); // Assuming userId is passed as a parameter

        if (userId == null || userId.isEmpty()) {
            ((HttpServletResponse) response).setStatus(400);
            response.getWriter().write("{\"code\": 400, \"msg\": \"Missing userId\"}");
            return;
        }

        long currentTime = System.currentTimeMillis() / 1000;
        userRequestCounts.putIfAbsent(userId, 0L);
        userLastRequestTime.putIfAbsent(userId, currentTime);

        synchronized (this) {
            long lastRequestTime = userLastRequestTime.get(userId);
            long requests = userRequestCounts.get(userId);

            if (currentTime - lastRequestTime <= RATE_LIMIT_WINDOW_SECONDS) {
                if (requests >= MAX_REQUESTS_PER_WINDOW) {
                    ((HttpServletResponse) response).setStatus(429);
                    response.getWriter().write("{\"code\": 429, \"msg\": \"Too many requests\"}");
                    return;
                }
                userRequestCounts.put(userId, requests + 1);
            } else {
                userRequestCounts.put(userId, 1L);
                userLastRequestTime.put(userId, currentTime);
            }
        }

        chain.doFilter(request, response);
    }
}
