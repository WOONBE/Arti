package com.d106.arti.global.config;

import com.d106.arti.global.interceptor.ImageInterceptor;
import com.d106.arti.global.interceptor.StaticRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final StaticRequestInterceptor staticRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ImageInterceptor()).order(1).addPathPatterns("/static/**");
//        registry.addInterceptor(staticRequestInterceptor).order(2).addPathPatterns("/static/**");
    }
}
