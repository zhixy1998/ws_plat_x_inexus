package com.persee.workstation.demo.config;

import com.persee.workstation.demo.interceptor.StompLogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * STOMP over WebSocket 配置
 * <p>
 * 参考：
 * - https://docs.spring.io/spring/reference/web/websocket/stomp/enable.html
 * - https://docs.spring.io/spring/reference/web/websocket/stomp/interceptors.html
 * - https://docs.spring.io/spring/reference/web/websocket/stomp/configuration-performance.html
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    /**
     * 注册 STOMP 端点，前端用 ws://host:port/ws 连接
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }

    /**
     * 配置消息 broker：
     * - /app 前缀路由到 @MessageMapping
     * - /topic 和 /queue 走 SimpleBroker，开启心跳（10s/10s）
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic", "/queue")
                .setHeartbeatValue(new long[]{10000, 10000})  // 服务端发/收心跳间隔
                .setTaskScheduler(heartbeatScheduler());
    }

    /**
     * 在 clientInboundChannel 注册拦截器，打印每条入站 STOMP 帧
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new StompLogInterceptor());
    }

    /**
     * 配置传输层参数
     * 参考：https://docs.spring.io/spring/reference/web/websocket/stomp/configuration-performance.html
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(128 * 1024)        // 单条 STOMP 消息最大 128KB
                .setSendTimeLimit(15 * 1000)             // 单条消息发送超时 15s
                .setSendBufferSizeLimit(512 * 1024);     // 发送缓冲区 512KB
    }

    /**
     * 心跳专用调度器
     */
    @Bean
    public ThreadPoolTaskScheduler heartbeatScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("ws-heartbeat-");
        scheduler.initialize();
        return scheduler;
    }
}
