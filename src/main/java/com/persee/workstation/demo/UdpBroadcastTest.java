package com.persee.workstation.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.*;

public class UdpBroadcastTest {

    public static void main(String[] args) throws Exception {
        int port = 9999;

        // 1. 获取本机局域网 IPv4 地址
        String localIp = getLocalLanIp();
        System.out.println("本机 IP: " + localIp);
        System.out.println("监听端口: " + port);

        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            System.out.println("[服务端] 开始监听广播...");
            byte[] buf = new byte[1024];
            while (true) {
                DatagramPacket recvPacket = new DatagramPacket(buf, buf.length);
                serverSocket.receive(recvPacket);
                String msg = new String(recvPacket.getData(), 0, recvPacket.getLength());
                System.out.println("[接收] 来自 " + recvPacket.getAddress().getHostAddress()
                        + ":" + recvPacket.getPort() + " → " + msg);
                if ("UV_INEXUS_DISCOVER".equals(msg)) {
                    byte[] replyData = localIp.getBytes();
                    DatagramPacket replyPacket = new DatagramPacket(
                            replyData, replyData.length,
                            recvPacket.getAddress(), recvPacket.getPort()
                    );
                    serverSocket.send(replyPacket);
                    System.out.println("[回复] → " + recvPacket.getAddress().getHostAddress()
                            + ":" + recvPacket.getPort() + " 内容: " + localIp);
                }
            }

        }
    }
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
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
