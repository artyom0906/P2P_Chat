package com.artyom_and_zayac.p2p.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;

public class Client implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private DatagramSocket socket;
    private InetAddress address;

    public Client() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            logger.error(e.getMessage());
        }
        try {
            address = InetAddress.getByName("213.200.50.96");
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
        }

        new Thread(this).start();
    }

    @Override
    public void run() {
        String msg = "Hello";
        try {
            DatagramPacket packet
                    = new DatagramPacket(msg.getBytes(), msg.getBytes().length,
                    address, 7000);
            socket.send(packet);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
