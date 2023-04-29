package com.example.networkedcrossword;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Game implements Serializable {
    private int dim;
    public int seed;
    private int row;
    private int col;
    private Dict dict;
    public ArrayList<ArrayList<String>> board_words;
    private String[][] boardSeed;
    private String[][] untouchedBoard;
    public String[][] copyGameBoard;

    public int turn = 1;

    public String[][] boardSolution;

    public boolean isServer = false;

    public Game(int dim, int seed){
        this.seed = seed;
        this.row = dim;
        this.col = dim;
        this.dict = new Dict(seed);
        this.generateStructure();
        this.untouchedBoard = this.copyInitialArray();
        this.board_words = this.buildDictList();
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
        // [(1A)]
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
//        this.untouchedBoard = this.boardSeed;
        return this.boardSeed;
    }
    public String[][] getBoard() {
        return this.boardSeed;
    }
    public String[][] getUntouchedBoard() {
        return this.untouchedBoard;
    }

    public String[][] copyInitialArray(){
        String[][] copy = new String[this.getBoard().length][this.getBoard().length];
        for (int i = 0; i < this.getBoard().length; i++) {
            String[] aString = this.getBoard()[i];
            int length = aString.length;
            copy[i] = new String[length];
            System.arraycopy(aString, 0, copy[i], 0, length);
        }
        return copy;
    }
    public String[][] handleBoardStateUpdate(boolean[] solved, ArrayList<ArrayList<String>> words) {

//        System.out.println(this.boardSeed);
//        System.out.println(this.seed);
        String[][] updatedGameBoard = this.getBoard();

        for (int i = 0; i < solved.length; i++) {
            System.out.println(solved[i]);
            if (solved[i]) {
                String replaceWord = String.valueOf(words.get(i).get(2));
                System.out.println("replace word = " + replaceWord);
                int[] indices = this.findStartIndex(words.get(i).get(0), this.getUntouchedBoard());
                if (words.get(i).get(1).equals("A")) {
                    //replace across
                    for (int j = 0; j < replaceWord.length(); j++) {
                        System.out.println("indices[0] + " + indices[0] + " indices[1] " + indices[1]);
                        updatedGameBoard[indices[0]][indices[1]] = String.valueOf(replaceWord.charAt(j));
                        indices[0]++;
                    }
                } else {
                    //replace down
                    for (int k = 0; k < replaceWord.length(); k++) {
                        updatedGameBoard[indices[0]][indices[1]] = String.valueOf(replaceWord.charAt(k));
                        indices[1]++;
                    }
                }
            }
        }
        System.out.println(Arrays.toString(updatedGameBoard));

        return updatedGameBoard;
    }
    public int[] findStartIndex(String id, String[][] list) {
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list[0].length; j++) {
                if(id.equals(list[i][j])) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{};
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

    public void updateGame(boolean[] correctlyGuessedWords) {
        handleBoardStateUpdate(correctlyGuessedWords, this.board_words);
    }


}
