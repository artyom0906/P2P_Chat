package com.artyom_and_zayac.p2p.server;


import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        new Server();
        Thread.sleep(10000);
    }
}
