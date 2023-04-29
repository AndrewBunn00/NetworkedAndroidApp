package com.example.networkedcrossword;

import static java.lang.Math.min;
import static java.lang.Thread.sleep;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private NetworkThread serverThread;
    private NetworkThread clientThread;
    private Data data = new Data();
//    private int seed = 1;
    private int dim = 8;
    private Game clientMain;
    private int seed = (int) (Math.random() * 3) + 1;
    public boolean isPlayer2 = false;

    CrosswordBoard crosswordBoard;
    Game game;

    LinearLayout layout;
    GridView promptView;

//    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new Thread(updateTextWithTime).start();
        this.promptView = findViewById(R.id.promptView);
//        this.promptView.setVisibility(View.INVISIBLE);
        ((ViewGroup)promptView.getParent()).removeView(promptView);

        this.layout = findViewById(R.id.linLayout);
//        this.layout.setVisibility(View.INVISIBLE);
        ((ViewGroup)layout.getParent()).removeView(layout);
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

            // update with the recieved data
//            HashMap<String, String> map = data.cleanUpDataString();
//            data.updateData(map);

            // update the game
            data.incrementTurn();

            System.out.println("Send the string over! " + text);

            // prep the data for sending
            String msg = data.toJson();
            data.setData(msg, true);
            data.set_disable_button(true);


            if(this.clientThread != null) {
                this.game = this.clientThread.assignClientGame(this.clientThread.client);
//                intent.putExtra("game", this.clientThread.assignClientGame(this.clientThread.client));
//                intent.putExtra("dataServerOrClient", clientThread.assignClientData());

            }
            else if(this.serverThread != null) {
                this.game = this.serverThread.serverGame;
//                intent.putExtra("game", this.serverThread.serverGame);
//                intent.putExtra("dataServerOrClient", serverThread.assignServerData());
            }


            removeEverythingFromScreen(textBox);
//
            // Screen size
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int windowHeight = displayMetrics.heightPixels;
            int windowWidth = displayMetrics.widthPixels;
            int offset = 20;

            // dimensions and offsets for prompt
            int size = min(windowHeight, windowWidth);
            int sizeUpdated = size - offset;
            int heightSizeUpdated = size/3 - offset;

            Point navBarSize = getNavigationBarSize(this);
            int heightNavBar = navBarSize.y + offset;

            // Readd the linLayout and the game

////            ((ViewGroup)layout.getParent()).removeView(layout);
////            ((ViewGroup)promptView.getParent()).removeView(promptView);
//
            promptView.setLayoutParams(new ViewGroup.LayoutParams(sizeUpdated, heightSizeUpdated));

            // Center the promptview at the bottom of the screen
            int promptHeight = windowHeight - heightSizeUpdated - heightNavBar;

            crosswordBoard = new CrosswordBoard(this);
            crosswordBoard.setAttributes(game.isServer ? 1 : 2, game, game.getBoard());

//        int promptHeight = windowHeight - heightSizeUpdated;
            promptView.setY(promptHeight);
            promptView.setX(offset/2);

            layout.addView(promptView);
            layout.addView(crosswordBoard);
            setContentView(this.layout);


            ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
            layoutParams.height = windowHeight;
            layoutParams.width = windowWidth;
            layout.setLayoutParams(layoutParams);

            crosswordBoard.setY(-(windowHeight/8));




        }

    }

    private void removeEverythingFromScreen(TextView textBox) {
        TextView title = findViewById(R.id.crosswordTitle);
        TextView code = findViewById(R.id.code);
        Button submit = findViewById(R.id.submit);
        Button createGameButton = findViewById(R.id.createGameButton);
        Button joinGameButton = findViewById(R.id.joinButton);
        Button button = findViewById(R.id.button);


        // remove everything from the screen
        ((ViewGroup) textBox.getParent()).removeView(textBox);
        ((ViewGroup)code.getParent()).removeView(code);
        ((ViewGroup)title.getParent()).removeView(title);
        ((ViewGroup)submit.getParent()).removeView(submit);
        ((ViewGroup)createGameButton.getParent()).removeView(createGameButton);
        ((ViewGroup)joinGameButton.getParent()).removeView(joinGameButton);
        ((ViewGroup)button.getParent()).removeView(button);
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

        serverThread = new NetworkThread("Server", text, "10.0.2.15", portNumber, data);
        serverThread.start();

    }



    public void joinGame(View view) {
        data.set_isplayer1(false);
        TextView codeTextBox = findViewById(R.id.code);
//        output.setText("You touched me");
        String text = codeTextBox.getText().toString();

        // Check that the code is the right length and alert if not
        if(text.length() != 4) {
            Toast.makeText(this, "Port Number must be length 4 and Match server port", Toast.LENGTH_SHORT).show();
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
        clientThread = new NetworkThread("Client", text, "10.0.2.2", portNumber, data);
        clientThread.start();


        // create game state client, give it board and context

    }

    public void startGameButton(View view) {
        System.out.println("ENTERING GAME WINDOW");
//        try {
//            sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("data", data);

//        System.out.println("CHECK IF EXISTS: " + serverThread.serverGame.isServer);

        if(this.clientThread != null) {
            intent.putExtra("game", this.clientThread.assignClientGame(this.clientThread.client));
            intent.putExtra("dataServerOrClient", clientThread.assignClientData());

        }
        else if(this.serverThread != null) {
            intent.putExtra("game", this.serverThread.serverGame);
            intent.putExtra("dataServerOrClient", serverThread.assignServerData());
        }
        startActivity(intent);
    }


    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    // Code below from https://stackoverflow.com/questions/36514167/how-to-really-get-the-navigation-bar-height-in-android
    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer)     Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            }
            catch (IllegalAccessException e) {}
            catch     (InvocationTargetException e) {}
            catch (NoSuchMethodException e) {}
        }

        return size;
    }

}