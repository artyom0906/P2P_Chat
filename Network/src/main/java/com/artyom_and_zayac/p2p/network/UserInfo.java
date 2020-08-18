package com.artyom_and_zayac.p2p.network;

import java.io.Serializable;
import java.net.InetAddress;

public record UserInfo(InetAddress address, int port) implements Serializable {
    @Override
    public String toString() {
        return "[" + address + "," + port + ']';
    }
}
