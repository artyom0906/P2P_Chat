package com.artyom_and_zayac.p2p.server;


import java.io.IOException;

public class Main {
    public Main(String address, int port, NetworkListener listener){

    }
    public static void main(String[] args) throws IOException, InterruptedException {
        new Server();
        Thread.sleep(10000);
        new Main("192.168.3.20", 80, new NetworkListener() {
            @Override
            public void onStart(Main main) {

            }

            @Override
            public void onClose(Main main) {

            }

            @Override
            public void onMessage(String message, Main main) {
                main.sendMessage();
            }

            @Override
            public void onException(Exception e, Main main) {

            }
        });
    }
    public void sendMessage(){
        System.out.println("sending");
    }
}
interface NetworkListener{
    public void onStart(Main main);
    public void onClose(Main main);
    public void onMessage(String message, Main main);
    public void onException(Exception e, Main main);
}
