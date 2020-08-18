package com.artyom_and_zayac.p2p.server;

import com.artyom_and_zayac.p2p.network.Network;
import com.artyom_and_zayac.p2p.network.NetworkListener;
import com.artyom_and_zayac.p2p.network.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;


public class Server implements NetworkListener {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final Set<UserInfo> usersInfo = new HashSet<>();
    public Server() throws UnknownHostException {
        logger.info("starting listening");
        new Thread(()->{
            Scanner scanner = new Scanner(System.in);
            for(;;){
                if(scanner.nextLine().equalsIgnoreCase("clear")) usersInfo.clear();
            }
        }).start();
    }

    @Override
    public void onReceive(byte[] data, Network network, InetAddress address, int port) {
        logger.info(new String(data));
        try {
            UserInfo info = new UserInfo(address, port);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write("syn:".getBytes());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(info);
            usersInfo.stream().filter(userInfo -> !userInfo.equals(info))
                    .limit(100).forEach(userInfo -> {
                try {
                    objectOutputStream.writeObject(userInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            logger.info(String.valueOf(byteArrayOutputStream.toByteArray().length));
            network.sendMessage(byteArrayOutputStream.toByteArray(), address, port);

            usersInfo.add(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Exception e, Network network) {
        logger.error(e.getMessage(), e);
    }
}
