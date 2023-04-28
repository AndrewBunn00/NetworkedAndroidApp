package com.example.networkedcrossword;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private int dim;
    private int seed;
    private int row;
    private int col;
    private Dict dict;
    private String[][] boardSeed;
    public Game(int dim, int seed){
        this.seed = seed;
        this.row = dim;
        this.col = dim;
        this.dict = new Dict(seed);
        this.generateStructure();
    }
    private ArrayList<ArrayList<String>> buildDictList() {
        return this.dict.build();
    }
    private String[][] generateStructure() {
        if(this.seed == 1) {
            this.boardSeed = new String[row][col];
            this.boardSeed[0] = new String[]{"1", "*", "*", "*", "*", "*", "*", "*"};
            this.boardSeed[1] = new String[]{"-", "*", "*", "*", "*", "*", "*", "*"};
            this.boardSeed[2] = new String[]{"-", "*", "*", "*", "*", "*", "*", "*"};
            this.boardSeed[3] = new String[]{"11", "-", "-", "-", "-", "-", "-", "*"};
            this.boardSeed[4] = new String[]{"*", "*", "*", "12", "-", "-", "*", "*"};
            this.boardSeed[5] = new String[]{"*", "14", "15", "*", "*", "*", "16", "17"};
            this.boardSeed[6] = new String[]{"18", "-", "-", "*", "*", "*", "-", "-"};
            this.boardSeed[7] = new String[]{"20", "-", "-", "*", "21", "-", "-", "-"};
        }
        if(this.seed ==2) {
            this.row = 7;
            this.col = 7;
            this.boardSeed = new String[row][col];
            this.boardSeed[0] = new String[]{"1", "-", "-", "4", "-", "6", "*"};
            this.boardSeed[1] = new String[]{"-", "*", "*", "-", "*", "-", "8"};
            this.boardSeed[2] = new String[]{"-", "*", "*", "-", "*", "-", "-"};
            this.boardSeed[3] = new String[]{"10", "-", "-", "*", "11", "-", "-"};
            this.boardSeed[4] = new String[]{"-", "*", "*", "13", "*", "-", "-"};
            this.boardSeed[5] = new String[]{"14", "-", "-", "-", "-", "-", "*"};
            this.boardSeed[6] = new String[]{"*", "15", "-", "-", "-", "*", "*"};
        }

        if(this.seed == 3) {
            this.row = 6;
            this.col = 6;
            this.boardSeed = new String[row][col];
            this.boardSeed[0] = new String[]{"1", "2", "-", "-", "5", "6"};
            this.boardSeed[1] = new String[]{"7", "-", "-", "-", "-", "-"};
            this.boardSeed[2] = new String[]{"8", "-", "-", "-", "-", "-"};
            this.boardSeed[3] = new String[]{"9", "-", "-", "-", "-", "-"};
            this.boardSeed[4] = new String[]{"*", "10", "-", "-", "-", "*"};
            this.boardSeed[5] = new String[]{"*", "11", "-", "-", "-", "*"};
        }

        return this.boardSeed;
    }
    public void handleBoardStateUpdate(){
        System.out.println(this.boardSeed);
        System.out.println(this.seed);
    }

    public void handlePlayerTurn(String word) {

    }
}
