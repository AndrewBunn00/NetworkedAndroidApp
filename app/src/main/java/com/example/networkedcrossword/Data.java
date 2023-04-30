package com.example.networkedcrossword;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Data implements Serializable {
    // variables
    private String data = new String();
    private String[] params = new String[]{"player1_guess", "player2_guess", "player1_score", "player2_score", "turn", "total_words"};
    private String player1_guess = new String();
    private String player2_guess = new String();
    private int player1_score = 0;
    private int player2_score = 0;
    private int turn = 0;
    private boolean can_write = false;
    private boolean can_read = false;
    private boolean disable_button = false;
    private boolean is_player1 = false;

    private boolean endTurnHit = false;

    private boolean recievedUpdate = false;

    public boolean stopThread = false;

    private String boardStats = "";

    public Set<String> guessedWords = new HashSet<>();
    public boolean[] correctlyGuessedWords = new boolean[10];

    private int total_words = 0;

    // constructor
    public Data() {
    }

    // getters and setters
    public String getData() {
        return data;
    }

    public void setData(String data, boolean write) {
        this.data = data;
        this.can_write = write;
    }

    public void setDataOnly(String theData) {
        // Get the new data out and ready to be used
        HashMap<String, String> map = this.cleanUpDataString(theData);

        // Update the data
        this.updateData(map);

//        this.data = data;
    }


    public boolean isCan_write() {
        return can_write;
    }
    public void setCan_write(boolean value) {
        this.can_write = value;
    }
    public boolean setGuess(String guess){
        if(!guess.isEmpty()) {
            if(this.turn == 0) {
                //player 1 server
                this.player1_guess = guess;
                return true;

            } else {
                //player 2 client
                this.player2_guess = guess;
                return true;
            }
        } else {
            //guess is blank and return error to have user enter again
            return false;
        }
    }

    public void setPlayer1_score(){
        this.player1_score++;
    }
    public void setPlayer2_score(){
        this.player2_score++;
    }

    public int checkWin(){
        if((this.player1_score + this.player2_score) != 10) {
            return -1;
        }
        return (this.player1_score>this.player2_score) ? 1 : 2;
    }
    public void setScore() {
        if(this.turn == 0) {
            //player 1
            this.player1_score++;
        } else {
            //player 2
            this.player2_score++;
        }
    }

    // turn object into json string
    public String toJson() {
        HashMap<String, String> map = new HashMap<>();

        map.put("player1_guess",this.player1_guess);
        map.put("player2_guess",this.player2_guess);
        map.put("player1_score", String.valueOf(this.player1_score));
        map.put("player2_score", String.valueOf(this.player2_score));
        map.put("turn", String.valueOf(this.turn));
        map.put("total_words", String.valueOf(this.total_words));
        String words = Arrays.toString(this.correctlyGuessedWords);
        words = words.replace(",", ";");
        map.put("correctlyGuessedWords", words);



        JSONObject json = new JSONObject(map);
        return json.toString();

    }


    /**
     * clean up data string and return a map of all the values
     * @return
     */
    public HashMap<String, String> cleanUpDataString(String theData) {
        String[] partialSplit = theData.split(",");
        String[] values;
        HashMap<String, String> map = new HashMap<>();


        for(String s : partialSplit) {
            values = s.split(":");

            if(values[0].contains("{")) {
                values[0] = values[0].replace("{", "");
            }
            if(values[1].contains("}")) {
                values[1] = values[1].replace("}", "");
            }
            if(values[1].contains("[")) {
                values[1] = values[1].replace("[", "");
            }
            if(values[1].contains("]")) {
                values[1] = values[1].replace("]", "");
            }

            values[0] = values[0].replace("\"", "");
            values[1] = values[1].replace("\"", "");

            map.put(values[0], values[1]);
            System.out.println(values[0] + " " + values[1]);
        }
        return map;
    }

    /**
     * update data from json string
     * @param map
     */
    public void updateData(HashMap<String, String> map) {
        this.player1_guess = map.get("player1_guess");
        this.player2_guess = map.get("player2_guess");
        this.player1_score = Integer.parseInt(map.get("player1_score"));
        this.player2_score = Integer.parseInt(map.get("player2_score"));
        this.turn = Integer.parseInt(map.get("turn"));
        this.total_words = Integer.parseInt(map.get("total_words"));

        // TODO: rebuild boolean array and set it
        boolean[] correctlyGuessedWordsPlaceholder = new boolean[10];
        String strOfBools = map.get("correctlyGuessedWords");
        String[] indivBools = strOfBools.split(";");
        for(int i = 0; i < correctlyGuessedWordsPlaceholder.length; i++) {
            if(indivBools[i].contains("true")) {
                correctlyGuessedWordsPlaceholder[i] = true;
            }
            else {
                correctlyGuessedWordsPlaceholder[i] = false;
            }
        }

        this.correctlyGuessedWords = correctlyGuessedWordsPlaceholder;

    }
    public int getPlayer1_score(){
        return this.player1_score;
    }
    public int getPlayer2_score(){
        return this.player2_score;
    }
    public void incrementTurn() {
        this.turn++;
    }

    public void set_read(boolean value) {
        this.can_read = value;
    }

    public boolean can_read() {
        return can_read;
    }

    public void set_disable_button(boolean value) {
        this.disable_button = value;
    }

    public boolean disable_button() {
        return this.disable_button;
    }
    public boolean is_player1() {
        return is_player1;
    }
    public void set_isplayer1(boolean value) {
        this.is_player1 = value;
    }

    public void setBoardStats(String stats) {
        this.boardStats = stats;
    }

    public String getBoardStats() {
        return this.boardStats;
    }

    public void setRecievedUpdate(boolean value) {
        this.recievedUpdate = value;
    }

    public boolean getRecievedUpdate() {
        return this.recievedUpdate;
    }

    public void setEndTurnHit(boolean value) {
        this.endTurnHit = value;
    }

    public boolean getEndTurnHit() {
        return this.endTurnHit;
    }
}
