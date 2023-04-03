package com.example.networkedcrossword;

import java.io.IOException;
import java.net.Socket;

public class Client {
    Socket client;
    private int port;
    private String ip;

    public Client(String ip, int port, String message) {
        this.port = port;
        this.ip = ip;

        System.out.println("Client created on " + ip + ":" + port + " with message " + message);
    }

    public void clientStart() {
        System.out.println("Starting client\n");

        try {
            client = new Socket(ip, port);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
