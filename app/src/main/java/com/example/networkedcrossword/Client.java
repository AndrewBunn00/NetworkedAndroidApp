package com.example.networkedcrossword;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

import android.widget.TextView;

public class Client {
    Socket client;
    private int port;
    private String ip;

    private BufferedReader readIn;
    private BufferedWriter writeOut;
    private Data data;

    public Game game;

    public Client(String ip, int port, String message, Data data) {
        this.port = port;
        this.ip = ip;
        this.data = data;
        System.out.println("Client created on " + ip + ":" + port + " with message " + message);
    }

    public Game clientGame() {return this.game;}

    public void clientStart() {
        System.out.println("Starting client\n");

        try {
            client = new Socket(ip, port);
            System.out.println("Client connected to " + ip + ":" + port);
            readIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
            writeOut = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            int i = 0;
            data.set_read(true);

            // read the boardstats from the server FIRST READ ONLY
            String boardStats = readIn.readLine();
            data.setBoardStats(boardStats);
            String[] split = data.getBoardStats().split(",");
            int dim = Integer.parseInt(split[0]);
            int seed = Integer.parseInt(split[1]);
            this.game = new Game(dim, seed);
            System.out.println("Stuff" + game.isServer);

            // Read lines from the server RECIEVE SEND
            while (true) {
                sleep(1000);
                if(data.can_read()) {
                    receiveServerMessages();
                    data.set_read(false);
                    data.set_disable_button(false);
                    data.set_isplayer1(true);
                    data.setEndTurnHit(false);
                }
                if(data.isCan_write()) {
                    sendServerMessages(i);
                    data.setCan_write(false);
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

    private void receiveServerMessages() {
        try {
            System.out.println("[Client] right before readline");
            String read = readIn.readLine();
            System.out.println("[] Client received: " + read + "\n");
            data.setDataOnly(read);
            data.setRecievedUpdate(true);
            game.updateGame(data.correctlyGuessedWords);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendServerMessages(int i) {
        try {
            writeOut.write(data.getData() + "\n");
            writeOut.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Data getClientData() {
        return this.data;
    }


}

