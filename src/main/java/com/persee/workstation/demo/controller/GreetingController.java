package com.persee.workstation.demo.controller;

import com.persee.workstation.demo.model.OperationData;
import com.persee.workstation.demo.model.OperationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * STOMP 消息处理器
 * <p>
 * 客户端发到 /app/hello 的消息路由到这里，处理完返回 /topic/greetings。
 */
@Controller
public class GreetingController {
    private SimpMessagingTemplate template;

    @Autowired
    public GreetingController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/hello")
    public void greet(String greeting) {
        new Thread(() -> {
            try {
                for (int i = 1; i <= 20; i++) {
                    template.convertAndSend("/topic/greetings", i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
