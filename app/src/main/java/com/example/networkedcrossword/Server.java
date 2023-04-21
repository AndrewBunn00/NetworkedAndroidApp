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
    private BufferedWriter writeOut;
    public Server(int port) {
        this.port = port;
    }

    public void serverStart() {
        System.out.println("Starting server\n");

        try {
            serverSocket = new ServerSocket(port);
            waitForClientConnection();
            setupReadAndWrite();
            while(true) {
                sendClientMessages();
            }
//            sendClientMessages();
//            getClientMessages();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Client connected, start listening for messages

    }

    private void sendClientMessages() {
        try {
            writeOut.write("Hello from server\n");
            writeOut.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void setupReadAndWrite() {
        try {
            readIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writeOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
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
            System.out.println("readIn.readLine() failed");
            throw new RuntimeException(e);
        }
        System.out.println("MADE IT TO CLIENT MESSAGE STUFF\n");

        // while there are still messages, keep reading and sending them
        while(message != null) {
            System.out.println("Message from client: " + message);
            try{
                writeOut.write("Server received message: " + message + "\n");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
//            writeOut.println("Server received message: " + message);
            try {
                message = readIn.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        // if there are no more messages, close the connection
        System.out.println("Client disconnected");
        try {
            writeOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            readIn.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

