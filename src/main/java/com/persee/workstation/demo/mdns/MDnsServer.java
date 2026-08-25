package com.persee.workstation.demo.mdns;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
@Component("mDnsServer")
public class MDnsServer {

    private static JmDNS jmDNS;
    @PostConstruct
    public static void startMDns() throws Exception {
        InetAddress localAddress = InetAddress.getLocalHost();
        jmDNS = JmDNS.create(localAddress);

        Map<String, String> txtMap = new HashMap<>();
        txtMap.put("identifier", "adcb2779-9fb0-4bc7-9c78-9b6336e57208");
        txtMap.put("authTag", "dste4");
        txtMap.put("ver", "24");

        // 注册服务 _myapp._tcp.local.
        ServiceInfo serviceInfo = ServiceInfo.create(
                "_myapp._tcp.local.",
                "uniapp_mdns_service_android", // serviceName
                8080, //端口
                0,
                0,
                txtMap
        );

        jmDNS.registerService(serviceInfo);
        System.out.println("mDNS服务发布成功 _myapp._tcp port:8080");
    }

    @PreDestroy
    public static void stopMDns() {
        try {
            if(jmDNS != null) {
                jmDNS.unregisterAllServices();
                jmDNS.close();
                System.out.println("🔻mDNS服务停止  port:8080");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}