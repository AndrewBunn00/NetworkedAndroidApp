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
    private String[][] boardSeed;
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
    private String[][] generateStructure() {
        if(this.seed == 1) {
            this.boardSeed = new String[row][col];
            this.boardSeed[0] = new String[]{"1", "*", "*", "*", "*", "*", "*", "*"};
            this.boardSeed[1] = new String[]{"-", "*", "*", "*", "*", "*", "*", "*"};
            this.boardSeed[2] = new String[]{"-", "*", "*", "*", "*", "*", "*", "*"};
            this.boardSeed[3] = new String[]{"11", "-", "-", "-", "-", "-", "-", "*"};
            this.boardSeed[4] = new String[]{"*", "*", "*", "12", "-", "-", "*", "*"};
            this.boardSeed[5] = new String[]{""};
        }
        if(this.seed ==2) {

        }

        if(this.seed == 3) {

        }

        return this.boardSeed;
    }
    public void handleBoardStateUpdate(char[][] board){

    }

    public void handlePlayerTurn(String word) {

    }
}
