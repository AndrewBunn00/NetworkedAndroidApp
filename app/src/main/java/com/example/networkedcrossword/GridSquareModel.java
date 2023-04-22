package com.example.networkedcrossword;

// Model class is what handles data to be added to grid view items
public class GridSquareModel {
    int id;

    //  int x, int y, String letter, String clue, String word, int wordId, int direction?
    public GridSquareModel(int id) {
        this.id = id;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
