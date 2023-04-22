package com.example.networkedcrossword;

import static java.lang.Thread.sleep;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private NetworkThread serverThread;
    private NetworkThread clientThread;
//    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(updateTextWithTime).start();

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

        TextView textBox = findViewById(R.id.writeAnything);
//        output.setText("You touched me");
        String text = textBox.getText().toString();

        System.out.println("Send the string over! " + text);
    }


    public void createGame(View view) {

        TextView codeTextBox = findViewById(R.id.code);
        String text = codeTextBox.getText().toString();
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

        serverThread = new NetworkThread("Server", text, "10.0.2.15", portNumber);
        serverThread.start();

    }



    public void joinGame(View view) {

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
        clientThread = new NetworkThread("Client", text, "10.0.2.2", portNumber);
        clientThread.start();

    }

    public void startGameButton(View view) {
        System.out.println("ENTERING GAME WINDOW");
        startActivity(new android.content.Intent(this, GameActivity.class));
    }



}