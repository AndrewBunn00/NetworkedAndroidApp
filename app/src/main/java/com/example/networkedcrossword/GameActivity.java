package com.example.networkedcrossword;

import static java.lang.Math.min;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class GameActivity extends AppCompatActivity {
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        View boardView = findViewById(R.id.gameBoardView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int size = min(width, height);
        int offset = 20;

        System.out.println(size);
        boardView.setLayoutParams(new ViewGroup.LayoutParams(size-offset, size-offset));

        // Center the boardView
        boardView.setY((height - size) / 2);
        boardView.setX(offset/2);

        gridView = (GridView) findViewById(R.id.gameBoardView);
        Dict dict = new Dict(6, 9, this.getApplicationContext());
        try {
            dict.build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> word_set = dict.get_words();
        String[] word_list = new String[word_set.size()];
        HashMap<String, String> map = dict.get_dict();
        int count = 0;
        for(String word:word_set) {
            word_list[count++] = count + " _____" + map.get(word) + "Word length is " + word.length();
        }

//        for (int i = 0; i < word.length; i++) {
//            for (int j = 0; i < word.length; j++) {
//                word[i] = String.valueOf(j);
//            }
//        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,word_list);
        gridView.setAdapter(adapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(this.get, ""+word_list[i].toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}