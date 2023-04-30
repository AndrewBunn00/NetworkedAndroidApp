package com.example.networkedcrossword;

import android.content.Context;

import java.util.Objects;

public class NetworkThread extends Thread {
    private String serverOrClient;
    private String message;
    private String ip;
    private int port;
    private Data data;
    public Client client;

    public Server server;

    public Game clientGame;
    public Game serverGame;

//    public boolean stop = false;


    // Constructor
    public NetworkThread(String serverOrClient, String message, String ip,
                         int port, Data data) {
        this.serverOrClient = serverOrClient;
        this.message = message;
        this.ip = ip;
        this.port = port;
        this.data = data;
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

        // figure out whether we run server or client
        if(Objects.equals(serverOrClient, "Server")) {
            this.server = new Server(port, data);
            this.serverGame = server.game;
            server.serverStart();
        }
        else if(Objects.equals(serverOrClient, "Client")) {
            this.client = new Client(ip, port, message, data);

            int code = client.clientStart();
            if(code == -1) {
                System.out.println("Server not up yet NETWORK THREAD");
                Thread.currentThread().interrupt();
                return;
            }
        }
        else {
            System.out.println("Need to give string \"Server\" or \"Client\"");
            return;
        }

    }
    public Game assignClientGame(Client client) {
        return client.clientGame();
    }

    public Data assignClientData() {
        return client.getClientData();
    }

    public Data assignServerData() {
        return server.getServerData();
    }
}
