package com.example.networkedcrossword;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Game extends AppCompatActivity {
    private int dim;
    private int seed;
    private Context context;
    public Game(int dim, int seed, Context cxt){
        this.context = cxt;
        this.seed = seed;
    }

    public void handleBoardStateUpdate(char[][] board){

    }

    public void handlePlayerTurn(String word) {

    }
}
