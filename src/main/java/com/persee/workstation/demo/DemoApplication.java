package com.persee.workstation.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo 启动类
 * <p>
 * @EnableScheduling：开启 @Scheduled 定时任务，用于每隔几秒独立推一条心跳到 /topic/greetings。
 * 这是"与前端触发解耦"的链路验证——只要有客户端订阅了 /topic/greetings，
 * 它就应该每秒收到一条心跳，不需要客户端再发 /app/hello 触发任何行为。
 * 如果心跳都收不到，100% 是 broker→客户端 链路问题，跟业务代码无关。
 */
@SpringBootApplication
@RestController
@EnableScheduling
public class DemoApplication {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
