package com.game.fish.security;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//@Component
public class APITimestampFilter implements Filter {

    private static final String API_PASSWORD = "default_password"; // 你可以改为从配置文件中读取

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request,
                          jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String token = httpRequest.getHeader("token");
        String timestamp = httpRequest.getHeader("timestamp");

        if (token == null || timestamp == null) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.getWriter().write("{\"code\": 400, \"msg\": \"Missing token or timestamp\", \"data\": {}}");
            return;
        }

        String correctToken = generateToken(API_PASSWORD, timestamp);

        long currentTime = System.currentTimeMillis() / 1000;
        long requestTime = Long.parseLong(timestamp);

        if (Math.abs(currentTime - requestTime) > 60 || !token.equals(correctToken)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"code\": 401, \"msg\": \"Token invalid\", \"data\": {}}");
            return;
        }

        chain.doFilter(request, response);
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
}