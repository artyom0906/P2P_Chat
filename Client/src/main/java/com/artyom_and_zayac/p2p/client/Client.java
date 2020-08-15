package com.artyom_and_zayac.p2p.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
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
        try {
            DatagramPacket packet
                    = new DatagramPacket("init".getBytes(), "init".getBytes().length,
                    address, 7000);
            socket.send(packet);

            byte[] buf = new byte[4096];
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            logger.info("msg: " + packet.getLength() + ": " + new String(packet.getData(), 0, packet.getLength()));
            socket.receive(packet);
            String connections = new String(packet.getData(), 0, packet.getLength());
            logger.info("msg: " + packet.getLength() + ": " + connections);
            String[] connectionsArr = connections.substring(1).substring(1, connections.length() - 2).split(",");

            List<Connection> connectionList = new ArrayList<>();
            for (String connect : connectionsArr) {
                String ip_port[] = connect.replaceAll("[\\[\\]]", "").split(",");
                connectionList.add(new Connection(InetAddress.getByName(ip_port[0]), Integer.parseInt(ip_port[1])));
            }
            logger.info(String.format("select server:%s", connections));
            int con_n = scan.nextInt();
            if(con_n == -1){
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                startListenT(packet.getAddress(), packet.getPort());
                logger.info(new String(packet.getData()));
                while (true){
                    String msg = scan.nextLine();
                    packet = new DatagramPacket(msg.getBytes(),msg.getBytes().length, connectionList.get(con_n).address, connectionList.get(con_n).port);
                    socket.send(packet);
                }
            }else {
                logger.info("write a message:");
                String data = scan.nextLine();
                packet = new DatagramPacket(data.getBytes(),data.getBytes().length, connectionList.get(con_n).address, connectionList.get(con_n).port);
                socket.send(packet);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void startListenT(InetAddress address, int port) {
        new Thread(()->{
            try {
                while (true) {
                    byte[] buf = new byte[4096];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    logger.info(new String(packet.getData()));
                }
            }catch (Exception e){
                logger.info("communication error", e);
            }
        }).start();
    }

    private record Connection(InetAddress address, int port){

    }
}
