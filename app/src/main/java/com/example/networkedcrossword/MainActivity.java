package com.example.networkedcrossword;

import static java.lang.Math.min;
import static java.lang.Thread.sleep;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
    int clickIndex;
    int playerTurnNum = 1;
    LinearLayout layout;
    GridView promptView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.promptView = findViewById(R.id.promptView);
        ((ViewGroup)promptView.getParent()).removeView(promptView);

        this.layout = findViewById(R.id.linLayout);
        ((ViewGroup)layout.getParent()).removeView(layout);
    }


    public void submitOnClick(View v) {


        if(!data.disable_button() && data.is_player1()) {
//            TextView textBox = findViewById(R.id.writeAnything);
//        output.setText("You touched me");
//            String text = textBox.getText().toString();

            // update the game
            data.incrementTurn();

            System.out.println("Send the string over! ");

            // prep the data for sending
            String msg = data.toJson();
            data.setData(msg, true);
            data.set_disable_button(true);


            if (this.clientThread != null) {
                this.game = this.clientThread.assignClientGame(this.clientThread.client);
            } else if (this.serverThread != null) {
                this.game = this.serverThread.serverGame;
            }

            removeEverythingFromScreen();

            // Screen size
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int windowHeight = displayMetrics.heightPixels;
            int windowWidth = displayMetrics.widthPixels;
            int offset = 20;

            // dimensions and offsets for prompt
            int size = min(windowHeight, windowWidth);
            int sizeUpdated = size - offset;
            int heightSizeUpdated = size / 3 - offset;

            Point navBarSize = getNavigationBarSize(this);
            int heightNavBar = navBarSize.y + offset;
            System.out.println(android.os.Build.MODEL);
            if (android.os.Build.MODEL.equals("sdk_gphone_x86_64")) {
                heightNavBar = 400;
            }

            System.out.println("[HeightNavBar] " + heightNavBar + " [sizeUpdated] " + sizeUpdated);
            // Read the linLayout and the game

            promptView.setLayoutParams(new ViewGroup.LayoutParams(sizeUpdated, heightSizeUpdated));

            // Center the promptview at the bottom of the screen
            int promptHeight = windowHeight - heightSizeUpdated - heightNavBar;

            crosswordBoard = new CrosswordBoard(this);
            crosswordBoard.setAttributes(game.isServer ? 1 : 2, game, game.getBoard());

            promptView.setY(promptHeight);
            promptView.setX(offset / 2);

            layout.addView(promptView);
            layout.addView(crosswordBoard);
            setContentView(this.layout);


            ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
            layoutParams.height = windowHeight;
            layoutParams.width = windowWidth;
            layout.setLayoutParams(layoutParams);

            crosswordBoard.setY(-(windowHeight / 8));

            new Thread(updateGameView).start();

//            if(this.clientThread != null) {
//                TextView playerTurn = findViewById(R.id.player_turn);
//                playerTurn.setText("Player 1 Turn");
//            }
//            else if(this.serverThread != null) {
//                TextView playerTurn = findViewById(R.id.player_turn);
//                playerTurn.setText("Player 1 Turn");
//            }

            ArrayList<ArrayList<String>> list = game.board_words;
            String[] wordsToAdd = new String[game.board_words.size()];

            for (int i = 0; i < list.size(); i++) {
                wordsToAdd[i] = list.get(i).get(0) +
                        "" +
                        list.get(i).get(1) +
//                            ""+
//                            list.get(i).get(2)+
                        " " +
                        list.get(i).get(3);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordsToAdd);
            promptView.setAdapter(adapter);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter Guess");

            TextInputLayout input = new TextInputLayout(this);
            input.setHint("Click to enter");

            TextInputEditText textInputEditText = new TextInputEditText(this);
            input.addView(textInputEditText);

            AlertDialog dialogBox = builder.setView(input).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //check flag to see whose turn
                    if (game.isServer) {
                        //player 1
                        playerTurnNum = 1;
                        //check to see if word has been guessed
                        System.out.println("[onClick index value] " + clickIndex);
                        ArrayList<String> parts = list.get(clickIndex);
                        String checkIfGuessed = parts.get(0) + parts.get(1);
                        System.out.println("[Player 1 Guessed Correctly] " + textInputEditText.getText() + " == " + parts.get(2));
                        String[][] temp;
                        System.out.println("SET CONTAINS " + data.guessedWords.contains(checkIfGuessed));
                        if (!data.guessedWords.contains(checkIfGuessed)) {
                            //check to see if word matches correctly
                            String guess = String.valueOf(textInputEditText.getText()).toUpperCase();
                            if (guess.equals(parts.get(2).toUpperCase())) {
                                //player 1 is correct update the gameboard
                                System.out.println("[Player 1 Guessed Correctly] " + textInputEditText.getText() + " == " + parts.get(2));
                                data.guessedWords.add(checkIfGuessed);
                                data.correctlyGuessedWords[clickIndex] = true;
                                temp = game.handleBoardStateUpdate(data.correctlyGuessedWords, list);
//                                crosswordBoard.setAttributes(1, game, temp);
//                                TextView playerTurn = findViewById(R.id.player_turn);
//                                playerTurn.setText("Player 2 Turn");
//                                TextView playerTurn = findViewById(R.id.player_turn);
//                                playerTurn.setText("Player " + crosswordBoard.player + " turn");
                            } else {
                                textInputEditText.setText("");
                            }
                        }
                        //update gameboard state to be passed server <-> client

                    } else {
                        // player 2
                        playerTurnNum = 2;
                        //check to see if word has been guessed
                        System.out.println("[onClick index value] " + clickIndex);
                        ArrayList<String> parts = list.get(clickIndex);
                        String checkIfGuessed = parts.get(0) + parts.get(1);
                        System.out.println("[Player 2 Guessed Correctly] " + textInputEditText.getText() + " == " + parts.get(2));
                        String[][] temp;
                        if (!data.guessedWords.contains(checkIfGuessed)) {
                            //check to see if word matches correctly
                            //check to see if word matches correctly
                            String guess = String.valueOf(textInputEditText.getText()).toUpperCase();
                            if (guess.equals(parts.get(2).toUpperCase())) {
                                System.out.println("[Player 2 Guessed Correctly] " + textInputEditText.getText() + " == " + parts.get(2));
                                data.guessedWords.add(checkIfGuessed);
                                data.correctlyGuessedWords[clickIndex] = true;
                                //player 1 is correct update the gameboard
                                temp = game.handleBoardStateUpdate(data.correctlyGuessedWords, list);
//                                crosswordBoard.setAttributes(2, game, temp);
//                                TextView playerTurn = findViewById(R.id.player_turn);
//                                playerTurn.setText("Player " + crosswordBoard.player + " turn");
//                                TextView playerTurn = findViewById(R.id.player_turn);
//                                playerTurn.setText("Player 1 Turn");
                            } else {
                                textInputEditText.setText("");
                            }
                        }
                        //update gameboard state to be passed server <-> client
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //do nothing just clear text box.
                    textInputEditText.setText("");
                    dialogInterface.dismiss();
                }
            }).create();

            promptView.setOnItemClickListener((adapterView, view, i, l) -> {
                try {
                    clickIndex = i;
                    dialogBox.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }

    private void removeEverythingFromScreen() {
        TextView title = findViewById(R.id.crosswordTitle);
        TextView code = findViewById(R.id.code);
        Button submit = findViewById(R.id.submit);
        Button createGameButton = findViewById(R.id.createGameButton);
        Button joinGameButton = findViewById(R.id.joinButton);
//        Button button = findViewById(R.id.button);


        // remove everything from the screen
//        ((ViewGroup) textBox.getParent()).removeView(textBox);
        ((ViewGroup)code.getParent()).removeView(code);
        ((ViewGroup)title.getParent()).removeView(title);
        ((ViewGroup)submit.getParent()).removeView(submit);
        ((ViewGroup)createGameButton.getParent()).removeView(createGameButton);
        ((ViewGroup)joinGameButton.getParent()).removeView(joinGameButton);
//        ((ViewGroup)button.getParent()).removeView(button);
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
        String text = codeTextBox.getText().toString();

        // Check that the code is the right length and alert if not
        if(text.length() != 4) {
            Toast.makeText(this, "Port Number must be length 4 and Match server port", Toast.LENGTH_SHORT).show();
            return;
        }

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

    }

    public void startGameButton(View view) {
//        System.out.println("ENTERING GAME WINDOW");
//
//        Intent intent = new Intent(this, GameActivity.class);
//        intent.putExtra("data", data);
//
////        System.out.println("CHECK IF EXISTS: " + serverThread.serverGame.isServer);
//
//        if(this.clientThread != null) {
//            intent.putExtra("game", this.clientThread.assignClientGame(this.clientThread.client));
//            intent.putExtra("dataServerOrClient", clientThread.assignClientData());
//
//        }
//        else if(this.serverThread != null) {
//            intent.putExtra("game", this.serverThread.serverGame);
//            intent.putExtra("dataServerOrClient", serverThread.assignServerData());
//        }
//        startActivity(intent);
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


    public void onClickEndTurnMainActivity(View view) {
        // update the game
        data.incrementTurn();

        // prep the data for sending
        String msg = data.toJson();
        data.setData(msg, true);
    }


    public final Runnable updateGameView = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // Need to use runOnUiThread to update UI, as main thread must do it
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() { crosswordBoard.invalidate(); }
                });
            }
        }
    };



}