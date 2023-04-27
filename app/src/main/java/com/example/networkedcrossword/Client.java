package com.example.networkedcrossword;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Client {
    Socket client;
    private int port;
    private String ip;

    private BufferedReader readIn;
    private BufferedWriter writeOut;
    private Data data;
    private Data old_data;

    public Client(String ip, int port, String message, Data data) {
        this.port = port;
        this.ip = ip;
        this.data = data;

        System.out.println("Client created on " + ip + ":" + port + " with message " + message);
    }

    public void clientStart() {
        System.out.println("Starting client\n");

        try {
            client = new Socket(ip, port);
            System.out.println("Client connected to " + ip + ":" + port);
            readIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
            writeOut = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            int i = 0;
            data.set_read(true);

            // Read lines from the server RECIEVE SEND
            while (true) {
                sleep(1000);
                // if(data.can_read_client())
                if(data.can_read()) {
                    receiveServerMessages();
//                    data.set_read_client(false);
                    data.set_read(false);
                    data.set_disable_button(false);
                    data.set_isplayer1(true);
                }
//                sendServerMessages(i);
                if(data.isCan_write()) {
                    sendServerMessages(i);
                    data.setCan_write(false);
//                    data.set_read_client(true);
                    data.set_read(true);
                }
                i++;
            }
//            client.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public boolean data_diff() {
        //compare values from old_data and data

        return true;
    }

    private void receiveServerMessages() {
        try {
            String read = readIn.readLine();
            System.out.println("[] Client received: " + read + "\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendServerMessages(int i) {
        try {
//            writeOut.write("Hello from client " + data.getData() + "\n");
            writeOut.write("Hello from client " + data.getData() + "\n");
            writeOut.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

