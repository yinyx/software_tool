package com.nari.software_tool.Interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author yinyx
 * @version 1.0 2020/4/15
 */
@Configuration
public class WebApplicationConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/login",
                "/user/login",
                "/user/loginOut",
                "/**/*.css",
                "/**/*.js",
                "/**/*.jpg",
                "/**.*.png",
                "/**/*.gif",
                "/**/*.map");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}
