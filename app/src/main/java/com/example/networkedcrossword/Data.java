package com.example.networkedcrossword;

import org.json.JSONObject;

import java.util.HashMap;

public class Data {
    // variables
    private String data = new String();
    private String[] params = new String[]{"player1_guess", "player2_guess", "player1_score", "player2_score", "turn", "total_words"};
    private String player1_guess = new String();
    private String player2_guess = new String();
    private int player1_score = 0;
    private int player2_score = 0;
    private int turn;
    private boolean can_write = false;
    private boolean can_read = false;
    private boolean disable_button = false;
    private boolean is_player1 = false;

    private String boardStats = "";

    private int total_words;

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

    public void setDataOnly(String data) {
        this.data = data;
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
    public boolean complete() {
        return (this.player1_score + this.player2_score) == (this.total_words) ;
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

        // player:word
//        String key = arg.split(":")[0];
//        String value = arg.split(":")[1];
        map.put("player1_guess",this.player1_guess);
        map.put("player2_guess",this.player2_guess);
        map.put("player1_score", String.valueOf(this.player1_score));
        map.put("player2_score", String.valueOf(this.player2_score));
        map.put("turn", String.valueOf(this.turn));
        map.put("total_words", String.valueOf(this.total_words));


        JSONObject json = new JSONObject(map);
//        System.out.println("JSON: " + json);
        return json.toString();

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
}
