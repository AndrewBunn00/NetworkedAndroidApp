package com.example.networkedcrossword;

import static java.lang.Math.min;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;

public class GameActivity extends AppCompatActivity {
    CrosswordBoard crosswordBoard;
    int clickIndex;

//    Data data = new Data();
//    Data networkData;
//    Game game;

    NetworkThread thread;
    Client client;
    Server server;
    Game newGame;
    Data newData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

//        LinearLayout layout = new LinearLayout(this);
//        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        layout.setOrientation(LinearLayout.VERTICAL);

        this.thread = (NetworkThread) getIntent().getSerializableExtra("thread");

        if(Objects.equals(thread.serverOrClient, "Server")) {
            this.newData = thread.getData();
            this.newGame = thread.serverGame;
        }
        else {
            this.newData = thread.getData();
            this.newGame = thread.clientGame;
        }

        // Variables for placing prompt in right place
        int offset = 20;
        Point navBarSize = getNavigationBarSize(this);
        int heightNavBar = navBarSize.y + offset;



        // Screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int windowHeight = displayMetrics.heightPixels;
        int windowWidth = displayMetrics.widthPixels;

        // dimensions and offsets for prompt
        int size = min(windowHeight, windowWidth);
        int sizeUpdated = size - offset;
        int heightSizeUpdated = size/3 - offset;

        GridView promptView = findViewById(R.id.promptView);

        promptView.setLayoutParams(new ViewGroup.LayoutParams(sizeUpdated, heightSizeUpdated));

        // Center the promptview at the bottom of the screen
        int promptHeight = windowHeight - heightSizeUpdated - heightNavBar;

//        int promptHeight = windowHeight - heightSizeUpdated;
        promptView.setY(promptHeight);
        promptView.setX(offset/2);



//        this.data = (Data) getIntent().getSerializableExtra("data");
//        this.game = (Game) getIntent().getSerializableExtra("game");


//        this.networkData = (Data) getIntent().getSerializableExtra("dataServerOrClient");

        TextView whoAmIText = findViewById(R.id.whoAmIText);
        if(thread.serverOrClient == "Server") {
//            whoAmIText.bringToFront();
            whoAmIText.setText("Player 1");
        }
        else {
//            whoAmIText.bringToFront();
            whoAmIText.setText("Player 2");
        }


        // make the crossword view
        crosswordBoard = new CrosswordBoard(this);
        crosswordBoard.setAttributes(newGame.isServer ? 1 : 2, newGame, newGame.getBoard());
//        setContentView(crosswordBoard);


        // remove views from the activity and add to the layout
        LinearLayout layout = findViewById(R.id.linLayout);
        Button endTurnButton = findViewById(R.id.endTurnButton);

        ((ViewGroup)layout.getParent()).removeView(layout);
        ((ViewGroup)promptView.getParent()).removeView(promptView);
//        ((ViewGroup)endTurnButton.getParent()).removeView(endTurnButton);


        ArrayList<ArrayList<String>> list = newGame.board_words;
        String[] wordsToAdd = new String[newGame.board_words.size()];

        for (int i = 0; i < list.size(); i++) {
            wordsToAdd[i] = list.get(i).get(0)+
                            ""+
                            list.get(i).get(1)+
//                            ""+
//                            list.get(i).get(2)+
                            " "+
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
                if(newGame.isServer) {
                    //player 1
                    //check to see if word has been guessed
                    System.out.println("[onClick index value] "+ clickIndex);
                    ArrayList<String> parts = list.get(clickIndex);
                    String checkIfGuessed = parts.get(0)+parts.get(1);
                    System.out.println("[Player 1 Guessed Correctly] " + textInputEditText.getText() + " == " + parts.get(2));
                    String[][] temp;
                    System.out.println("SET CONTAINS " + newData.guessedWords.contains(checkIfGuessed));
                    if(!newData.guessedWords.contains(checkIfGuessed)) {
                        //check to see if word matches correctly
                        String guess = String.valueOf(textInputEditText.getText()).toUpperCase();
                        if(guess.equals(parts.get(2).toUpperCase())) {
                            //player 1 is correct update the gameboard
                            System.out.println("[Player 1 Guessed Correctly] " + textInputEditText.getText() + " == " + parts.get(2));
                            newData.guessedWords.add(checkIfGuessed);
                            newData.correctlyGuessedWords[clickIndex] = true;
                            temp = newGame.handleBoardStateUpdate(newData.correctlyGuessedWords, list);
                            crosswordBoard.setAttributes(1, newGame, temp);
                        } else {
                            textInputEditText.setText("");
                        }
                    }
                    //update gameboard state to be passed server <-> client

                } else {
                    // player 2
                    //check to see if word has been guessed
                    System.out.println("[onClick index value] " + clickIndex);
                    ArrayList<String> parts = list.get(clickIndex);
                    String checkIfGuessed = parts.get(0)+parts.get(1);
                    System.out.println("[Player 2 Guessed Correctly] " + textInputEditText.getText() + " == " + parts.get(2));
                    String[][] temp;
                    if(!newData.guessedWords.contains(checkIfGuessed)) {
                        //check to see if word matches correctly
                        //check to see if word matches correctly
                        String guess = String.valueOf(textInputEditText.getText()).toUpperCase();
                        if(guess.equals(parts.get(2).toUpperCase())) {
                            System.out.println("[Player 2 Guessed Correctly] " + textInputEditText.getText() + " == " + parts.get(2));
                            newData.guessedWords.add(checkIfGuessed);
                            newData.correctlyGuessedWords[clickIndex] = true;
                            //player 1 is correct update the gameboard
                            temp = newGame.handleBoardStateUpdate(newData.correctlyGuessedWords, list);
                            crosswordBoard.setAttributes(2, newGame, temp);
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
//                System.out.println("right about show " + i);
                dialogBox.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        layout.addView(promptView);
//        layout.addView(endTurnButton);
        layout.addView(crosswordBoard);
        setContentView(layout);

        ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
        layoutParams.height = windowHeight;
        layoutParams.width = windowWidth;
        layout.setLayoutParams(layoutParams);

        crosswordBoard.setY(-(windowHeight/8));

//        endTurnButton.setX(windowWidth - windowWidth/3);
//        endTurnButton.setY(windowHeight - windowHeight/2);
//        endTurnButton.bringToFront();


//        promptView.setY(windowHeight - windowHeight/3);

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


    public void onClickEndTurn(View view) {
        System.out.println("END TURN");

//        if(data.getRecievedUpdate()) {
//            this.game.updateGame(data.correctlyGuessedWords);
//            crosswordBoard.invalidate();
//            data.setRecievedUpdate(false);
//        }

        // update the game
        newData.incrementTurn();

        // prep the data for sending
        String msg = newData.toJson() + "\n";
        newData.setData(msg, true);
//        networkData.setCan_write(true);
//        data.set_disable_button(true);
        System.out.println("DONE WITH BUTTON");


    }


}
