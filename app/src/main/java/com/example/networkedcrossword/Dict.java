package com.example.networkedcrossword;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Dict extends AppCompatActivity {
    private Context context;
    private ArrayList<ArrayList<String>> boards;
    ArrayList<String> file = null;
    private int seed;
    public Dict(int seed,Context cxt){
        this.context = cxt;
        this.boards = new ArrayList<>(10);
        this.seed = seed;
    }

    public ArrayList<ArrayList<String>> build() throws IOException {
        for (int i = 0; i < 10; i++) {
            boards.add(new ArrayList<>());
        }
        InputStream is = null;
        BufferedReader reader = null;
        try {
            switch (seed) {
                case 1:
                    is = context.getResources().openRawResource(R.raw.board1);
                    break;
            }
            reader = new BufferedReader(new InputStreamReader(is));
            String read;
            file = new ArrayList<String>();
            while ((read = reader.readLine()) != null) {
                file.add(read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("[HERE]" + is);
            assert is != null;
            is.close();
        }

        Random rand = new Random();
        final long start_mSecs = System.currentTimeMillis();
        /*
        1:A:Belowdeck:Television show about a service staff on a yacht
        [1, A, Belowdeck, Television show about a service staff on a yacht]
         */
        int count = 0;
        for (String line : file) {
            String[] parts = line.split(":");
            for (int i = 0; i < parts.length; i++) {
                boards.get(count).add(parts[i]);
            }
            count++;
        }
        System.out.println(boards);
        return boards;
    }
}