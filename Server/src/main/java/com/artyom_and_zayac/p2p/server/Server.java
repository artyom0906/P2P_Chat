package com.artyom_and_zayac.p2p.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private final DatagramSocket serverSocket;
    private static final List<String> usersInfo = new ArrayList<>();
    public Server() throws IOException {
        serverSocket = new DatagramSocket(7000);//213.200.50.96
        logger.info("test");
        System.out.println("test");
        new Thread(this).start();
    }
    @Override
    public void run() {
        while (true){
            try {
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                logger.info("wait for packet");
                serverSocket.receive(packet);
                logger.info("receive packet");
                InetAddress inetAddress = packet.getAddress();
                int port = packet.getPort();
                byte[] data = packet.getData();
                logger.info("msg:"+new String(data));
                String userInfo = String.format("[%s, %s]", inetAddress, port);
                usersInfo.add(userInfo);
                String userInfoToSend = String.format("syncServer:%s", userInfo);
                packet = new DatagramPacket(userInfoToSend.getBytes(), userInfoToSend.length(), inetAddress, port);
                serverSocket.send(packet);

                String usersInfoSend = usersInfo.toString();

                packet = new DatagramPacket(userInfoToSend.getBytes(), userInfoToSend.length(), inetAddress, port);

            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
    }
}
