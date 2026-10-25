package com.hotel.config;

import com.hotel.web.AuthInterceptor;
import com.hotel.web.CsrfInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String[] STATIC_AND_ERROR = {"/css/**", "/js/**", "/error", "/favicon.ico"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. every form POST must carry the session's CSRF token
        registry.addInterceptor(new CsrfInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(STATIC_AND_ERROR);

        // 2. everything except the login page needs a signed-in user
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login")
                .excludePathPatterns(STATIC_AND_ERROR);
    }
}
