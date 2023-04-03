package com.example.networkedcrossword;

import java.util.Objects;

public class NetworkThread extends Thread {
    private String serverOrClient;
    private String message;
    private String ip;
    private int port;

    // Constructor
    public NetworkThread(String serverOrClient, String message, String ip, int port) {
        this.serverOrClient = serverOrClient;
        this.message = message;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        // Check if any of the parameters are empty
        if(serverOrClient == null || serverOrClient.isEmpty()) {
            System.out.println("Server or client is empty");
            return;
        }

        if(message == null || message.isEmpty()) {
            System.out.println("Message is empty");
            return;
        }

        if(ip == null || ip.isEmpty()) {
            System.out.println("IP is empty");
            return;
        }

        if(port == 0) {
            System.out.println("Port is 0");
            return;
        }

//        System.out.println("Sending message: " + message + " to " + ip + ":" + port);

        // figure out whether we run server or client
        if(Objects.equals(serverOrClient, "Server")) {
            Server server = new Server(port);
            server.serverStart();
        }
        else if(Objects.equals(serverOrClient, "Client")) {
            Client client = new Client(ip, port, message);
            client.clientStart();
        }
        else {
            System.out.println("Need to give string \"Server\" or \"Client\"");
            return;
        }

    }
}
