package com.example.networkedcrossword;

import static java.lang.Math.min;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        View boardView = findViewById(R.id.gameBoardView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int size = min(width, height);
        int offset = 20;

        System.out.println(size);
        boardView.setLayoutParams(new ViewGroup.LayoutParams(size-offset, size-offset));

        // Center the boardView
        boardView.setY((height - size) / 2);
        boardView.setX(offset/2);
    }
}