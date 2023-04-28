package com.example.networkedcrossword;

import static java.lang.Thread.sleep;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private NetworkThread serverThread;
    private NetworkThread clientThread;
    private Data data = new Data();
    private int seed = 1;
    private int dim = 8;
//    private int seed = (int) (Math.random() * 4) + 1;
    public boolean isPlayer2 = false;
//    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new Thread(updateTextWithTime).start();
    }

    // TODO: Clean up if interrupted
    public final Runnable updateTextWithTime = new Runnable() {
        Integer i = 0;

        @Override
        public void run() {
            TextView output = findViewById(R.id.writeAnything);
            while (true) {
                i++;
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Need to use runOnUiThread to update UI, as main thread must do it
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        output.setText(i.toString());
                    }
                });
            }

        }
        // Check if submit button hit, if so, stop the thread (will want later for timer possibly)
//        if (Thread.interrupted()) {
//            System.out.println("Thread interrupted");
//            break;
//        }
    };


    public void submitOnClick(View view) {

        if(!data.disable_button() && data.is_player1()) {
            TextView textBox = findViewById(R.id.writeAnything);
//        output.setText("You touched me");
            String text = textBox.getText().toString();

            data.incrementTurn();

            System.out.println("Send the string over! " + text);

            String msg = data.toJson();
            data.setData(msg, true);
            data.set_disable_button(true);
        }

    }
// row/col num : a indicates across, d down :
    // needs row col and seedNum for a game obj

    public void createGame(View view) {
        data.set_isplayer1(true);
        TextView codeTextBox = findViewById(R.id.code);
        String text = codeTextBox.getText().toString();
        StringBuilder str = new StringBuilder();
        str.append(dim);
        str.append(",");
        str.append(seed);
        data.setBoardStats(str.toString());
        // Check that the code is the right length and alert if not
        if(text.length() != 4) {
            Toast.makeText(this, "Port number must be length 4", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println("Starting a server, on port number: " + text);

        // TODO: call server code here
        // create server
        int portNumber;
        try {
            portNumber = Integer.parseInt(text);
        }
        catch (NumberFormatException e) {
            portNumber = 1880;
        }

        serverThread = new NetworkThread("Server", text, "10.0.2.15", portNumber, data, this);
        serverThread.start();

    }



    public void joinGame(View view) {
        data.set_isplayer1(false);
        TextView codeTextBox = findViewById(R.id.code);
//        output.setText("You touched me");
        String text = codeTextBox.getText().toString();

        // Check that the code is the right length and alert if not
        if(text.length() != 4) {
            Toast.makeText(this, "Port Numbermust be length 4 and Match server port", Toast.LENGTH_SHORT).show();
            return;
        }

        // System.out.println("Joining server with code: " + text);
        int portNumber;
        try {
            portNumber = Integer.parseInt(text);
        }
        catch (NumberFormatException e) {
            portNumber = 1880;
        }

        // TODO: call client code here
        clientThread = new NetworkThread("Client", text, "10.0.2.2", portNumber, data, this);
        clientThread.start();
        // create game state client, give it board and context

    }

    public void startGameButton(View view) {
        System.out.println("ENTERING GAME WINDOW");
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("data", data);

//        System.out.println("CHECK IF EXISTS: " + serverThread.serverGame.isServer);

        if(this.clientThread != null) {
            intent.putExtra("game", this.clientThread.clientGame);
        }
        else if(this.serverThread != null) {
            intent.putExtra("game", this.serverThread.serverGame);
        }


        startActivity(intent);
    }



}