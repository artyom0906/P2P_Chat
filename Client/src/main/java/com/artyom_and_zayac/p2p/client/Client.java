package com.artyom_and_zayac.p2p.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Scanner;

public class Client implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private DatagramSocket socket;
    private InetAddress address;
    private Scanner scan = new Scanner(System.in);

    public Client() {
        logger.info("Running constructor");
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
        while(true) {
            String msg = scan.nextLine();
            try {
                DatagramPacket packet
                        = new DatagramPacket(msg.getBytes(), msg.getBytes().length,
                        address, 7000);
                socket.send(packet);

                byte[]buf = new byte[4096];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                logger.info("msg: " + packet.getLength() + ": " +new String(packet.getData(), 0, packet.getLength()));
                socket.receive(packet);
                logger.info("msg: " + packet.getLength() + ": " +new String(packet.getData(), 0, packet.getLength()));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
