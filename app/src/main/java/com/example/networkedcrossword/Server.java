package com.example.networkedcrossword;

// import java.io.DataOutputStream;
// import java.io.IOException;
// import java.net.ServerSocket;
// import java.net.Socket;
import static java.lang.Thread.sleep;

import java.io.*;
import java.net.*;

public class Server {
    private int port;
    ServerSocket serverSocket;
    Socket clientSocket;
    private BufferedReader readIn;
    private BufferedWriter writeOut;
    private Data data;
    public Server(int port, Data data) {
        this.port = port;
        this.data = data;
    }

    public void serverStart() {
        System.out.println("Starting server\n");

        try {
            serverSocket = new ServerSocket(port);
            waitForClientConnection();
            System.out.println("Client connected\n");
            setupReadAndWrite();
            int i = 0;

            // SEND RECIEVE
            while(true) {
                sleep(1000);
                if(data.isCan_write()) {
                    System.out.println("INSIDE CAN WRITE FLAG SERVER");
                    sendClientMessages(i);
                    data.setCan_write();
                    data.set_read_server(true);
                }
//                else {
//                    continue;
//                }
//                sendClientMessages(i);
                if(data.can_read_server()) {
                    receiveClientMessages();
                    data.set_read_server(false);
                }
                i++;
            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Client connected, start listening for messages

    }

    private void receiveClientMessages() {
        try {
            String read = readIn.readLine();
            System.out.println("[] Server received: " + read + "\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendClientMessages(int i) {
        try {
//            writeOut.write("Hello from server " + data.getData() + "\n");
            writeOut.write("Hello from server " + i + "\n");

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

