package com.artyom_and_zayac.p2p.server;


import com.artyom_and_zayac.p2p.network.Network;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        new Network(7000, new Server());
    }
}
