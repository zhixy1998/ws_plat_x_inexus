package com.persee.workstation.demo.common.config;

import com.persee.workstation.demo.common.interceptor.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC 配置：注册公共拦截器。
 * 按 Spring Framework 官方文档「Interceptors」标准写法：
 * 实现 WebMvcConfigurer，重写 addInterceptors 注册拦截器。
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 日志+耗时拦截器：作用于 /api/** 接口
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/api/**");
    }
}
