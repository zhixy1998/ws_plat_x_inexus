package com.persee.workstation.demo;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class MDnsServer {

    private static JmDNS jmDNS;

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

    // 关闭，程序关闭调用
    public static void stopMDns() {
        try {
            if(jmDNS != null) {
                jmDNS.unregisterAllServices();
                jmDNS.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        startMDns();
    }
}