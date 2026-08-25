package com.persee.workstation.demo;

import java.net.*;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * UDP 广播测试 —— 获取本机局域网 IP 并发送/接收 UDP 数据包
 * <p>
 * 直接跑 main 就行，不需要启动 Spring 容器。
 */
public class UdpBroadcastTest1 {

    public static void main(String[] args) throws Exception {
        int port = 41082;

        // 1. 获取本机局域网 IPv4 地址
        String localIp = getLocalLanIp();
        System.out.println("本机 IP: " + localIp);
        System.out.println("目标端口: " + port);

        try (DatagramSocket socket = new DatagramSocket(port)) {
            socket.setBroadcast(true);
            socket.setSoTimeout(500);

            System.out.println("[服务端] 启动，监听 " + port + "，同时广播本机 IP...");
            byte[] buf = new byte[1024];

            long lastHeartbeatTime = 0;
            long heartbeatTimeout = 5000;
            long lastBroadcastTime = 0;
            long broadcastInterval = 1000;

            while (true) {
                try {
                    DatagramPacket recvPacket = new DatagramPacket(buf, buf.length);
                    socket.receive(recvPacket);

                    if (recvPacket.getAddress().getHostAddress().equals(localIp)) {
                        continue;
                    }

                    String msg = new String(recvPacket.getData(), 0, recvPacket.getLength());
                    System.out.println("[接收] 来自 " + recvPacket.getAddress().getHostAddress()
                            + ":" + recvPacket.getPort() + " → " + msg);

                    lastHeartbeatTime = System.currentTimeMillis();

                    // byte[] replyData = localIp.getBytes();
                    // DatagramPacket replyPacket = new DatagramPacket(
                    // replyData, replyData.length,
                    // recvPacket.getAddress(), recvPacket.getPort()
                    // );
                    // socket.send(replyPacket);
                    // System.out.println("[回复] → " + recvPacket.getAddress().getHostAddress()
                    // + ":" + recvPacket.getPort() + " 内容: " + localIp);
                } catch (SocketTimeoutException e) {
                    // receive 超时，继续往下检查是否需要广播
                }

                long now = System.currentTimeMillis();
                boolean appOnline = lastHeartbeatTime > 0
                        && (now - lastHeartbeatTime < heartbeatTimeout);

                if (!appOnline && now - lastBroadcastTime > broadcastInterval) {
                    byte[] data = localIp.getBytes();
                    DatagramPacket broadcastPacket = new DatagramPacket(
                            data, data.length,
                            InetAddress.getByName("255.255.255.255"), port);
                    socket.send(broadcastPacket);
                    System.out.println("[广播] → 255.255.255.255:" + port + " 内容: " + localIp);
                    lastBroadcastTime = now;
                }
            }
        }
    }

    @GetMapping("/hello")
    public Map<String, Object> hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Hello " + name + "!");
        return result;
    }

    /**
     * 遍历网卡获取本机第一个可用的局域网 IPv4 地址
     */
    public static String getLocalLanIp() throws SocketException {
        var interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            if (ni.isLoopback() || !ni.isUp()) {
                continue;
            }
            var addresses = ni.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                    return addr.getHostAddress();
                }
            }
        }
        return "127.0.0.1";
    }
}
