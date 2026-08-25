package com.persee.workstation.demo.mqtt; //改成你自己的包名

import io.moquette.broker.Server;
import io.moquette.broker.config.IConfig;
import io.moquette.broker.config.MemoryConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * 内嵌 Moquette Broker。
 * 注意：必须在 bean 初始化阶段启动（@PostConstruct），不能用 ApplicationRunner——
 * 后者在容器完全启动后才执行，而 MQTT 入站适配器在 Spring 生命周期阶段就会去连接
 * broker，会导致 "Connection refused"（32103）。
 */
@Component("embeddedMqttBroker")
public class MoquetteBrokerRunner {

    private final Server mqttBroker = new Server();

    @PostConstruct
    public void start() throws IOException {
        Properties props = new Properties();
        props.setProperty(IConfig.PORT_PROPERTY_NAME, "1883");
        props.setProperty(IConfig.WEB_SOCKET_PORT_PROPERTY_NAME, "8083");
        props.setProperty(IConfig.ALLOW_ANONYMOUS_PROPERTY_NAME, "true");

        IConfig config = new MemoryConfig(props);
        mqttBroker.startServer(config);
        System.out.println("✅ Moquette Broker启动成功，监听：tcp://127.0.0.1:1883");
    }

    @PreDestroy
    public void stop() {
        mqttBroker.stopServer();
        System.out.println("🔻 Moquette Broker已停止");
    }
}
