package com.persee.workstation.demo.mqtt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

/**
 * 订阅消息处理
 * 所有 mqttInputChannel 的消息都会进入这里
 */
@Component
public class MqttSubscriber implements MessageHandler {

    @Autowired
    private MqttGateway gateway;

    @Override
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) {
        String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
        Integer qos = (Integer) message.getHeaders().get("mqtt_receivedQos");
        Object payload = message.getPayload();

        System.out.printf("[MQTT 收到] topic=%s, qos=%s, payload=%s%n", topic, qos, payload);

        // 路由：根据 topic 分发到不同业务处理
        if ("instrument/status".equals(topic)) {
            // 触发事件给业务监听器处理
            new Thread(() -> {
                try {
                    for (int i = 1; i <= 20; i++) {
                        gateway.sendToMqtt("instrument/notify", 0, "1");
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        } else if ("app/request".equals(topic)) {
            // App 主动请求 → 回复当前状态
            // publisher.publish("instrument/notify", "当前状态 OK");
        } else if ("instrument/data".equals(topic)) {
            // 仪器上报数据 → 入库
        }
    }
}
