package com.persee.workstation.demo.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import java.nio.charset.StandardCharsets;

/**
 * 入站 STOMP 帧日志拦截器
 * <p>
 * 参考：https://docs.spring.io/spring/reference/web/websocket/stomp/interceptors.html
 * <p>
 * 拦截所有客户端发往服务端的 STOMP 帧（CONNECT/SUBSCRIBE/SEND/UNSUBSCRIBE/DISCONNECT 等），
 * 打印命令、会话 ID、destination 和消息体。
 */
public class StompLogInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        String sessionId = accessor.getSessionId();
        String destination = accessor.getDestination();

        // 心跳帧没有 command，跳过避免日志刷屏
        if (command == null) {
            return message;
        }

        StringBuilder log = new StringBuilder();
        log.append("[STOMP-IN] cmd=").append(command);
        if (sessionId != null) {
            log.append(", session=").append(sessionId.substring(0, Math.min(8, sessionId.length())));
        }
        if (destination != null) {
            log.append(", destination=").append(destination);
        }

        // SEND 帧带 payload，打印前 500 字符
        Object payload = message.getPayload();
        if (payload instanceof byte[]) {
            String body = new String((byte[]) payload, StandardCharsets.UTF_8);
            if (!body.isEmpty()) {
                log.append(", payload=").append(body.length() > 500
                        ? body.substring(0, 500) + "...(" + body.length() + "B)"
                        : body);
            }
        } else if (payload != null) {
            log.append(", payload=").append(payload);
        }

        System.out.println(log);
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        // 不打印出站消息（出站都由 Controller 触发，Controller 自己已经打印了）
    }
}
