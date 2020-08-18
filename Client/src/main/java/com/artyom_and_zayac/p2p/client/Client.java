package com.artyom_and_zayac.p2p.client;

import com.artyom_and_zayac.p2p.network.Network;
import com.artyom_and_zayac.p2p.network.NetworkListener;
import com.artyom_and_zayac.p2p.network.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client implements NetworkListener {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private static UserInfo userInfo = null;
    List<UserInfo> users = new ArrayList<>();
    private static Thread listener = null;

    @Override
    public void onReceive(byte[] data, Network network, InetAddress address, int port) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        byte[] typeBuf = new byte[4];
        byteArrayInputStream.read(typeBuf, 0, 4);
        String type = new String(typeBuf);
        logger.info(type);
        if (type.equals("syn:")) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                userInfo = (UserInfo) objectInputStream.readObject();

                logger.info(userInfo.toString());

                UserInfo info;
                do {
                    try {
                        info = (UserInfo) objectInputStream.readObject();
                        if (info == null) break;
                        users.add(info);
                    } catch (Exception e) {
                        info = null;
                    }
                } while (info != null);
                logger.info(users.toString());
                Scanner scanner = new Scanner(System.in);
                int id;
                id = scanner.nextInt();
                if(id != -1) {
                    network.sendMessage("usr:hello", InetAddress.getLocalHost(), users.get(id).port());
                    if(listener == null) {
                        listener = new Thread(() -> {
                            while (true) {
                                String msg = scanner.nextLine();
                                try {
                                    network.sendMessage(msg, InetAddress.getLocalHost(), users.get(id).port());
                                } catch (UnknownHostException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        listener.start();
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }else if (type.equals("usr:")){
            logger.info("user");
            logger.info(new String(byteArrayInputStream.readAllBytes()));
            Scanner scanner = new Scanner(System.in);
            if(listener == null) {
                listener = new Thread(() -> {
                    while (true) {
                        String msg = scanner.nextLine();
                        network.sendMessage(msg, address, port);
                    }
                });
                listener.start();
            }
        }
    }

    @Override
    public void onError(Exception e, Network network) {

    }
}
