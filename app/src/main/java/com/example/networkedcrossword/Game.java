package com.example.networkedcrossword;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private int dim;
    public int seed;
    private int row;
    private int col;
    private Dict dict;
    private String[][] boardSeed;
    public int turn = 1;

    public String[][] boardSolution;

    public boolean isServer = false;

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
            this.boardSeed[0] = new String[]{"1", "-", "-", "11", "*", "*", "18", "20"};
            this.boardSeed[1] = new String[]{"*", "*", "*", "-", "*", "14", "-", "-"};
            this.boardSeed[2] = new String[]{"*", "*", "*", "-", "*", "15", "-", "-"};
            this.boardSeed[3] = new String[]{"*", "*", "*", "-", "12", "*", "*", "*"};
            this.boardSeed[4] = new String[]{"*", "*", "*", "-", "-", "*", "*", "21"};
            this.boardSeed[5] = new String[]{"*", "*", "*", "-", "-", "*", "*", "-"};
            this.boardSeed[6] = new String[]{"*", "*", "*", "-", "*", "16", "-", "-"};
            this.boardSeed[7] = new String[]{"*", "*", "*", "*", "*", "17", "-", "-"};
        }
        if(this.seed ==2) {
            this.row = 7;
            this.col = 7;
            this.boardSeed = new String[row][col];
            this.boardSeed[0] = new String[]{"1", "-", "-", "10", "-", "14", "*"};
            this.boardSeed[1] = new String[]{"-", "*", "*", "-", "*", "-", "15"};
            this.boardSeed[2] = new String[]{"-", "*", "*", "-", "*", "-", "-"};
            this.boardSeed[3] = new String[]{"4", "-", "-", "*", "13", "-", "-"};
            this.boardSeed[4] = new String[]{"-", "*", "*", "11", "*", "-", "-"};
            this.boardSeed[5] = new String[]{"6", "-", "-", "-", "-", "-", "*"};
            this.boardSeed[6] = new String[]{"*", "8", "-", "-", "-", "*", "*"};
        }

        if(this.seed == 3) {
            this.row = 6;
            this.col = 6;
            this.boardSeed = new String[row][col];
            this.boardSeed[0] = new String[]{"1", "7", "8", "9", "*", "*"};
            this.boardSeed[1] = new String[]{"2", "-", "-", "-", "10", "11"};
            this.boardSeed[2] = new String[]{"-", "-", "-", "-", "-", "-"};
            this.boardSeed[3] = new String[]{"-", "-", "-", "-", "-", "-"};
            this.boardSeed[4] = new String[]{"5", "-", "-", "-", "-", "-"};
            this.boardSeed[5] = new String[]{"6", "-", "-", "-", "*", "*"};
        }

        return this.boardSeed;
    }
    public String[][] getBoard() {
        return this.boardSeed;
    }
    public void handleBoardStateUpdate(){
        System.out.println(this.boardSeed);
        System.out.println(this.seed);
    }

    public void handlePlayerTurn(String word) {
        // Update the user turn
        if(this.turn == 1) {
            this.turn = 2;
        } else {
            this.turn = 1;
        }

        // Update the board with the selected word

    }
}
