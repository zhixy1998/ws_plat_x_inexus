package com.persee.workstation.demo.mqtt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.StringUtils;

@Configuration
@EnableAsync
@EnableIntegration
// @MessagingGateway 接口的代理由这个注解扫描生成；必须显式指定包，
// 否则默认只扫 config 包及子包，扫不到 service 包里的 MqttGateway
@IntegrationComponentScan(basePackages = "com.persee.workstation.demo.mqtt")
public class MqttConfig {
    @Value("${mqtt.broker-url}")
    private String brokerUrl;
    @Value("${mqtt.client-id}")
    private String clientId;
    @Value("${mqtt.username:}")
    private String username;
    @Value("${mqtt.password:}")
    private String password;
    @Value("${mqtt.default-qos:1}")
    private int defaultQos;
    @Value("${mqtt.default-topic}")
    private String defaultTopic;
    @Value("${mqtt.subscribe-topics:}")
    private String subscribeTopics;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{brokerUrl});
        options.setAutomaticReconnect(true);          // 自动重连
        options.setCleanSession(false);              // 保留会话，离线期间消息不丢
        options.setKeepAliveInterval(60);            // 60s 心跳
        options.setConnectionTimeout(30);             // 连接超时 30s
        if (StringUtils.hasText(username)) {
            options.setUserName(username);
            options.setPassword(password.toCharArray());
        }
        factory.setConnectionOptions(options);
        return factory;
    }

    // ========== 出站 Handler（核心：消息推送） ==========
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(clientId, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setAsyncEvents(true);                 // 投递成功/失败发事件
        messageHandler.setDefaultQos(defaultQos);
        messageHandler.setDefaultTopic(defaultTopic);
        messageHandler.setDefaultRetained(false); //不保留消息
        return messageHandler;
    }

    // ==========  出站通道（推送用） ==========
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }



    // ==========  入站通道（订阅用） ==========
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        String[] topics = subscribeTopics.split(",");
        int[] qos = new int[topics.length];
        for (int i = 0; i < topics.length; i++) qos[i] = defaultQos;
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(brokerUrl, clientId + "-sub",
                        topics);
        adapter.setCompletionTimeout(5000);
        // 注意：Spring Integration 7.x 已移除 setRecoveryInterval，
        // 断线重连由上面 MqttConnectOptions.setAutomaticReconnect(true) 负责

        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(qos);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println(message.getPayload());
            }

        };
    }
}
