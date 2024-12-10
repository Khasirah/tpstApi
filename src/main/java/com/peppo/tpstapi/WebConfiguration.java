package com.peppo.tpstapi;

import com.peppo.tpstapi.resolver.UserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private UserArgumentResolver userArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(userArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/surat/*/download")
            .allowedOrigins("http://localhost:5173", "http://localhost:5544", "http://10.6.254.253:5544")
            .allowedMethods("GET")
            .allowedHeaders("*")
            .allowCredentials(true);
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}
