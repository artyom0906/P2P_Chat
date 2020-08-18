package com.artyom_and_zayac.p2p.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Network implements AutoCloseable{
    private final int buffer_size;
    private DatagramSocket socket;
    private InetAddress inetAddress;
    private int port;
    private final Thread listenerThread;
    private boolean isRun;
    private final NetworkListener listener;
    private final static int DEFAULT_BUFFER_SIZE = 4096;

    public Network(InetAddress address, int port, NetworkListener listener) throws SocketException {
        this(new DatagramSocket(), DEFAULT_BUFFER_SIZE, listener, address, port);
    }

    public Network(InetAddress address, int port, int buffer_size, NetworkListener listener) throws SocketException {
        this(new DatagramSocket(port, address), buffer_size, listener, address, port);
    }

    public Network(DatagramSocket socket, NetworkListener listener){
        this(socket, DEFAULT_BUFFER_SIZE, listener, null, 0);
    }

    public Network(int port, NetworkListener listener) throws SocketException {
        this(new DatagramSocket(port), DEFAULT_BUFFER_SIZE, listener, null, port);
    }

    private Network(DatagramSocket socket, int buffer_size, NetworkListener listener, InetAddress address, int port){
        this.port = port;
        this.inetAddress = address;
        this.buffer_size = buffer_size;
        this.socket = socket;
        this.listener = listener;
        isRun=true;
        this.listenerThread = new Thread(()->{
            while (isRun){

                DatagramPacket packet = new DatagramPacket(new byte[buffer_size],buffer_size);
                try {
                    this.socket.receive(packet);
                    if(inetAddress==null) inetAddress = packet.getAddress();
                    if(this.port != 0) this.port=packet.getPort();
                    this.listener.onReceive(packet.getData(), this, packet.getAddress(), packet.getPort());
                } catch (IOException e) {
                    this.listener.onError(e, this);
                }
            }
        });
        this.listenerThread.start();
    }

    public void sendMessage(DatagramPacket packet){
        try {
            socket.send(packet);
        }catch (Exception e){
            listener.onError(e, this);
        }
    }
    public void sendMessage(String msg){
        this.sendMessage(msg, inetAddress, port);
    }

    public void sendMessage(String msg, InetAddress address, int port){
        this.sendMessage(msg.getBytes(), address, port);
    }

    public void sendMessage(byte[] msg, InetAddress address, int port){
        this.sendMessage(new DatagramPacket(msg, msg.length, address, port));
    }

    @Override
    public void close() throws Exception {
        if(listener instanceof ClosableNetworkListener closableNetworkListener) closableNetworkListener.onClose(this);
        socket.close();
        this.listenerThread.interrupt();
        this.isRun = false;
    }
}
