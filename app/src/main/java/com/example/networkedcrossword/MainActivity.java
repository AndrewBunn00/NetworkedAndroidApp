package com.example.networkedcrossword;

import static java.lang.Thread.sleep;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

//    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        handler = new Handler();
        new Thread(updateTextWithTime).start();

    }

    // TODO: Clean up if interrupted
    public Runnable updateTextWithTime = new Runnable() {
        Integer i = 0;

        @Override public void run() {
            TextView output = findViewById(R.id.writeAnything);
            while(true) {
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
//                System.out.println(i);
            }

        }
    };


    public void submitOnClick(View view) {

        TextView textBox = findViewById(R.id.writeAnything);
//        output.setText("You touched me");
        String text = textBox.getText().toString();

        System.out.println("Send the string over! " + text);
//        handler.post(runnable);
//        new Thread(runnable).start();
    }


    public void createGame(View view) {

        TextView codeTextBox = findViewById(R.id.code);
//        output.setText("You touched me");
        String text = codeTextBox.getText().toString();

        // Check that the code is the right length and alert if not
        if(text.length() != 3) {
            Toast.makeText(this, "Code must be length 3", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println("Starting a server, with join code: " + text);

        // TODO: call server code here

    }

    public void joinGame(View view) {

        TextView codeTextBox = findViewById(R.id.code);
//        output.setText("You touched me");
        String text = codeTextBox.getText().toString();

        // Check that the code is the right length and alert if not
        if(text.length() != 3) {
            Toast.makeText(this, "Code must be length 3", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println("Joining server with code: " + text);

        // TODO: call client code here

    }


}