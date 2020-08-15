package com.artyom_and_zayac.p2p.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Server implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private final DatagramSocket serverSocket;
    private static final HashSet usersInfo = new HashSet();
    public Server() throws IOException {
        serverSocket = new DatagramSocket(7000);
        new Thread(this).start();
    }
    @Override
    public void run() {
        while (true){
            try {
                byte[] buf = new byte[4096];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                logger.info("wait for packet");
                serverSocket.receive(packet);
                logger.info("receive packet");
                InetAddress inetAddress = packet.getAddress();
                int port = packet.getPort();
                byte[] data = packet.getData();
                logger.info("msg: " + packet.getLength() + ": " +new String(packet.getData(), 0, packet.getLength()));
                String userInfo = String.format("[%s, %s]", inetAddress.getHostAddress(), port);
                usersInfo.add(userInfo);

                String userInfoToSend = String.format("syncServer:%s", userInfo);
                byte[] dataToSend = userInfoToSend.getBytes();
                logger.info(new String(dataToSend));
                packet = new DatagramPacket(dataToSend,dataToSend.length, inetAddress, port);
                serverSocket.send(packet);

                String usersInfoToSend = usersInfo.toString();
                dataToSend = usersInfoToSend.getBytes();
                packet = new DatagramPacket(dataToSend, dataToSend.length, inetAddress, port);
                serverSocket.send(packet);


            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
    }
}
