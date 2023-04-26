package com.example.networkedcrossword;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Client {
    Socket client;
    private int port;
    private String ip;
    private Data data;
    private Data old_data;
    boolean flag;


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
            BufferedReader rdr = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter wrtr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            int i = 0;

            // Read lines from the server
            while (true) {
                sleep(1000);
                String read = "";
                if(data.can_read()) {
                    read = rdr.readLine();
                    System.out.println("[] Client received: " + read + i + "\n");
                    if (read == null) {
                        break;
                    }
                    data.set_read();

                }else if(data.isCan_write()) {
                    System.out.println("INSIDE CAN WRITE FLAG CLIENT");
                    wrtr.write("Hello from client " + read + "\n");
                    wrtr.flush();
                    data.setCan_write();
                } else {
                    continue;
                }
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
    public boolean data_diff() {
        //compare values from old_data and data

        return true;
    }
}

