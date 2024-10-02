package com.d106.arti.global.config;

import com.d106.arti.global.interceptor.ImageInterceptor;
import com.d106.arti.global.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new ImageInterceptor())
            .order(2)
            .addPathPatterns("/static");
    }
}
