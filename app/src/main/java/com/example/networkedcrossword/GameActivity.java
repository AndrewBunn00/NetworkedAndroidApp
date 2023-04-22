package com.example.networkedcrossword;

import static java.lang.Math.min;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class GameActivity extends AppCompatActivity {
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        View promptView = findViewById(R.id.promptView);
        View gameBoardView = findViewById(R.id.gameBoard);

        // Get the naviation bar size
        int offset = 20;
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        int heightNavigationBar = getResources().getDimensionPixelSize(resourceId) + offset;
        System.out.println("NAV BAR: " + heightNavigationBar);

        // Get the screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int windowHeight = displayMetrics.heightPixels;
        int windowWidth = displayMetrics.widthPixels;

        int size = min(windowWidth, windowHeight);
        int sizeUpdated = size - offset;
        int heightSizeUpdated = size/3 - offset;




        // set the size of the prompt view
        promptView.setLayoutParams(new ViewGroup.LayoutParams(sizeUpdated, heightSizeUpdated));

        // Center the promptView at the bottom of the screen
        int promptHeight = windowHeight - heightSizeUpdated - heightNavigationBar;
        promptView.setY(promptHeight);
        promptView.setX(offset/2);


        // set the size of the game board view
        gameBoardView.setLayoutParams(new ViewGroup.LayoutParams(sizeUpdated, sizeUpdated));

        // Center the gameBoardView between the top of the screen and the top of the promptView
        gameBoardView.setY(windowHeight - heightNavigationBar - size - heightSizeUpdated);
        gameBoardView.setX(offset/2);


        // Get the dictionary items and display it
        gridView = (GridView) findViewById(R.id.promptView);
        Dict dict = new Dict(6, 9, this.getApplicationContext());

        try {
            dict.build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Set<String> word_set = dict.get_words();
        String[] word_list = new String[word_set.size()];
        HashMap<String, String> map = dict.get_dict();

        int count = 0;
        for(String word:word_set) {
            word_list[count++] = count + " _____:" + map.get(word) + "Word length is " + word.length();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,word_list);
        gridView.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Word");
        TextInputLayout input = new TextInputLayout(this);
        EditText editText = new EditText(getApplicationContext());
        input.setHint("Words");
        input.addView(editText);

        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
              builder
                    .setTitle("Enter Word")
                    .setView(input)
                    .setMessage("Enter your Guess")
                    .setPositiveButton("Submit", (dialogInterface, i1) -> System.out.println("hello"))
                    .setNegativeButton("Cancel", (dialogInterface, i12) -> {
                    }).show();
        });
    }
}