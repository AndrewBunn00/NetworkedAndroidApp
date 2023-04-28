package com.example.networkedcrossword;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class Game extends AppCompatActivity {
    private int dim;
    private int seed;
    private Context context;
    private int row;
    private int col;
    private Dict dict;
    public Game(int dim, int seed, Context cxt){
        this.context = cxt;
        this.seed = seed;
        this.row = dim;
        this.col = dim;
        this.dict = new Dict(seed, this.context);
    }
    private ArrayList<ArrayList<String>> buildDictList() {
        ArrayList<ArrayList<String>> list;
        try {
            list = this.dict.build(); //2d arraylist
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public void handleBoardStateUpdate(char[][] board){

    }

    public void handlePlayerTurn(String word) {

    }
}
