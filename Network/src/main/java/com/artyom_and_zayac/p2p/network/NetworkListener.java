package com.artyom_and_zayac.p2p.network;

import java.net.InetAddress;

public interface NetworkListener {
    public void onReceive(byte[] data, Network network, InetAddress address, int port);
    public void onError(Exception e, Network network);
}
