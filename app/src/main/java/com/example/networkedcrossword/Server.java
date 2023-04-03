package com.example.networkedcrossword;

// import java.io.DataOutputStream;
// import java.io.IOException;
// import java.net.ServerSocket;
// import java.net.Socket;
import java.io.*;
import java.net.*;

public class Server {
    private int port;
    ServerSocket serverSocket;
    Socket clientSocket;
    private BufferedReader readIn;
    private PrintWriter writeOut;
    public Server(int port) {
        this.port = port;
    }

    public void serverStart() {
        System.out.println("Starting server\n");

        try {
            serverSocket = new ServerSocket(port);

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        waitForClientConnection();

        // Client connected, start listening for messages
        setupReadAndWrite();
        getClientMessages();
    }


    private void setupReadAndWrite() {
        try {
            readIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writeOut = new PrintWriter(clientSocket.getOutputStream(), true);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void waitForClientConnection() {
        try {
            // See if there is a client trying to connect
            clientSocket = serverSocket.accept();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void getClientMessages() {
        // get the first message
        String message;
        try {
            message = readIn.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("MADE IT TO CLIENT MESSAGE STUFF\n");

        // while there are still messages, keep reading and sending them
        while(message != null) {
            System.out.println("Message from client: " + message);
            writeOut.println("Server received message: " + message);
            try {
                message = readIn.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        // if there are no more messages, close the connection
        System.out.println("Client disconnected");
        writeOut.close();
        try {
            readIn.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
