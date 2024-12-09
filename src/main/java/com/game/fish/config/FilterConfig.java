package com.game.fish.config;

import com.game.fish.security.APITimestampFilter;
import com.game.fish.security.DynamicRateLimiter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<APITimestampFilter> apiTimestampFilter() {
        FilterRegistrationBean<APITimestampFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new APITimestampFilter());
        //"/user/*",
        registrationBean.addUrlPatterns(  "/fish/*", "/shop/*"); // 需要拦截的路径
        registrationBean.setOrder(1); // 设置优先级
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<DynamicRateLimiter> dynamicRateLimiter() {
        FilterRegistrationBean<DynamicRateLimiter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DynamicRateLimiter());
        registrationBean.addUrlPatterns("/*"); // 全局限流
        return registrationBean;
    }
}
