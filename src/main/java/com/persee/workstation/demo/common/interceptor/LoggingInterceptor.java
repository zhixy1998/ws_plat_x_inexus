package com.persee.workstation.demo.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 公共日志 + 耗时统计拦截器。
 * 完全按 Spring Framework 官方文档「Interceptors」标准写法实现：
 * - preHandle：请求开始，记录起始时间
 * - afterCompletion：请求结束，计算耗时并记录异常
 */
@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        request.setAttribute(START_TIME, System.currentTimeMillis());
        log.info("请求开始: {} {}", request.getMethod(), request.getRequestURI());
        return true; // 返回 true 继续处理
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        Long startTime = (Long) request.getAttribute(START_TIME);
        long duration = startTime == null ? 0 : System.currentTimeMillis() - startTime;
        if (ex != null) {
            log.error("请求异常: {} {}, 耗时 {}ms, error={}",
                    request.getMethod(), request.getRequestURI(), duration, ex.getMessage());
        } else {
            log.info("请求完成: {} {}, 耗时 {}ms",
                    request.getMethod(), request.getRequestURI(), duration);
        }
    }
}
