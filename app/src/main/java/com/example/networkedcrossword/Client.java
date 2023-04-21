package com.example.networkedcrossword;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

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
            BufferedWriter wrtr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            int i = 0;

            // Read lines from the server
            while (true) {
                sleep(1000);
                String read = rdr.readLine();
                System.out.println("[] Client received: " + read + i + "\n");
                if(read == null) {
                    break;
                }
                wrtr.write("Hello from client " + i + "\n");
                wrtr.flush();
                i++;
            }
            client.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

