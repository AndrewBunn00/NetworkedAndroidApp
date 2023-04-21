package com.example.networkedcrossword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            System.out.println("Client connected to " + ip + ":" + port);
            BufferedReader rdr = new BufferedReader(new InputStreamReader(client.getInputStream()));
            int i = 0;

            // Read lines from the server
            while (true) {
                String read = rdr.readLine();
                System.out.println("[maybe] Client received: " + read);
                if(read == null) {
                    break;
                }
                i++;
            }
            client.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

