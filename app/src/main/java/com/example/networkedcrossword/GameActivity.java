package com.example.networkedcrossword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Data data = (Data) getIntent().getSerializableExtra("data");
        Game game = (Game) getIntent().getSerializableExtra("game");


        TextView whoAmIText = findViewById(R.id.whoAmIText);
        if(game.isServer) {
            whoAmIText.setText("Server");
        }
        else {
            whoAmIText.setText("Client");
        }

    }
}