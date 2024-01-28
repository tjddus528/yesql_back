package com.appletantam.yesql_back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://yesql.co.kr", "http://yesql.co.kr","https://www.yesql.co.kr", "https://yesql.netlify.app/")
                .allowedMethods("OPTIONS","GET","POST","HEAD")
                .allowCredentials(true) // 쿠키 인증 요청 허용
                .maxAge(3600); // 3600 seconds
    }
}
