package com.example.networkedcrossword;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

public class GameActivity extends AppCompatActivity {
    CrosswordBoard crosswordBoard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Data data = (Data) getIntent().getSerializableExtra("data");
        Game game = (Game) getIntent().getSerializableExtra("game");

        TextView whoAmIText = findViewById(R.id.whoAmIText);
        if(game.isServer) {
            whoAmIText.setText("Player 1");
        }
        else {
            whoAmIText.setText("Player 2");
        }

        crosswordBoard = new CrosswordBoard(this);
        crosswordBoard.setAttributes(game.isServer ? 1 : 2, game);
        setContentView(crosswordBoard);




    }
}