package com.persee.workstation.demo.mqtt.controller;

import com.persee.workstation.demo.mqtt.service.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mqtt")
public class MqttController {
    @Autowired
    private MqttGateway gateway;
    /** 测试推送：http://localhost:8080/mqtt/send?msg=hello */
    @GetMapping("/send")
    public String send(@RequestParam String msg) {
        gateway.sendToMqtt(msg);
        return "已发送到默认主题: " + msg;
    }

    /** 自定义主题和 QoS */
    @PostMapping("/publish")
    public String publish(@RequestParam String topic,
                          @RequestBody String message,
                          @RequestParam(defaultValue = "1") int qos) {
        gateway.sendToMqtt(topic, qos, message);
        return String.format("已发送: topic=%s, qos=%d, msg=%s", topic, qos, message);
    }

    /** 发送保留消息 */
    @PostMapping("/publish-retained")
    public String publishRetained(@RequestParam String topic,
                                  @RequestBody String message,
                                  @RequestParam(defaultValue = "1") int qos) {
        gateway.sendToMqtt(topic, qos, message);
        return "已发送 retained 消息";
    }
}
