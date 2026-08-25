package com.persee.workstation.demo.listener;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

/**
 * STOMP 会话生命周期事件监听器
 * <p>
 * 参考：https://docs.spring.io/spring/reference/web/websocket/stomp/application-context-events.html
 * <p>
 * 监听事件：
 * - BrokerAvailabilityEvent: broker 可用性变化
 * - SessionConnectEvent: 客户端发起 CONNECT
 * - SessionConnectedEvent: 服务端回复 CONNECTED
 * - SessionSubscribeEvent: 客户端 SUBSCRIBE
 * - SessionUnsubscribeEvent: 客户端 UNSUBSCRIBE
 * - SessionDisconnectEvent: 客户端 DISCONNECT 或网络断开
 */
@Component
public class StompEventListener {

    private String shortId(String sessionId) {
        return sessionId != null ? sessionId.substring(0, Math.min(8, sessionId.length())) : "null";
    }

    @EventListener
    public void onBrokerAvailable(BrokerAvailabilityEvent event) {
        System.out.println("[STOMP-EVENT] Broker available=" + event.isBrokerAvailable());
    }

    @EventListener
    public void onConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("[STOMP-EVENT] CONNECT session=" + shortId(accessor.getSessionId())
                + ", user=" + accessor.getUser());
    }

    @EventListener
    public void onConnected(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("[STOMP-EVENT] CONNECTED session=" + shortId(accessor.getSessionId())
                + " (握手完成，会话已建立)");
    }

    @EventListener
    public void onSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("[STOMP-EVENT] SUBSCRIBE session=" + shortId(accessor.getSessionId())
                + ", destination=" + accessor.getDestination()
                + ", subId=" + accessor.getSubscriptionId());
    }

    @EventListener
    public void onUnsubscribe(SessionUnsubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("[STOMP-EVENT] UNSUBSCRIBE session=" + shortId(accessor.getSessionId())
                + ", subId=" + accessor.getSubscriptionId());
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("[STOMP-EVENT] DISCONNECT session=" + shortId(accessor.getSessionId())
                + ", reason=" + event.getCloseStatus() + ", origin=" + accessor.getSessionAttributes());
    }
}
