package com.artyom_and_zayac.p2p.client;

import com.artyom_and_zayac.p2p.network.Network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws Exception {
        Network network = new Network(InetAddress.getByName("213.200.50.96"), 7000, new Client());
        network.sendMessage("init");
    }
}
