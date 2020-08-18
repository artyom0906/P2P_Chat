package com.artyom_and_zayac.p2p.network;

public interface ClosableNetworkListener extends NetworkListener {
    public void onClose(Network network);
}
